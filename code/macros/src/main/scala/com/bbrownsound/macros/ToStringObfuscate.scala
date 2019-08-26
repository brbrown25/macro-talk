package com.bbrownsound.macros

import scala.annotation.StaticAnnotation
import scala.reflect.macros.whitebox

class ToStringObfuscate(fieldsToObfuscate: Any*) extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro ToStringObfuscateImpl.impl
}

object ToStringObfuscateImpl {
  def obfuscateValue(value: Any): String = {
    value match {
      case v: String => "*" * v.length
      case x => "*" * x.toString.length
    }
  }

  def impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[c.universe.ClassDef] = {
    import c.universe._

    def extractAnnotationParameters(tree: Tree): List[c.universe.Tree] = tree match {
      case q"new $name(..$params)  " => params
      case _ => throw new Exception("ToStringObfuscate annotation must have at least one parameter.")
    }

    // deconstructing things
    def extractCaseClassesParts(classDecl: ClassDef) = classDecl match {
      case q"case class $className(..$fields) extends ..$parents { ..$body}  " =>
        (className, fields, parents, body)
    }

    def replaceCaseClassSensitiveValues(tree: Tree) = tree match {
      case Literal(Constant(field: String)) =>
        q"""
          ${TermName(field)} = com.bbrownsound.macros.ToStringObfuscateImpl.obfuscateValue(this.${TermName(field)})
        """
      case _ => c.abort(c.enclosingPosition, s"[obfuscateValue] Match error with $tree")
    }

    def extractNewToString(sensitiveFields: List[Tree]) = {
      val fieldReplacements = sensitiveFields.map(replaceCaseClassSensitiveValues(_))
      q"""
       override def toString: ${typeOf[String]} = {
        scala.runtime.ScalaRunTime._toString(this.copy(..$fieldReplacements))
       }
      """
    }

    def modifiedDeclaration(classDecl: ClassDef): c.Expr[ClassDef] = {
      val (className, fields, parents, body) = extractCaseClassesParts(classDecl)
      val sensitiveFields = extractAnnotationParameters(c.prefix.tree)
      val newToString = extractNewToString(sensitiveFields)

      val params = fields
        .collect {
          case vd: ValDef => vd
        }
        .map(_.duplicate)

      //$ acts here as unquoting this basically constructs a new class def for us
      c.Expr[ClassDef](
        q"""
        case class $className (..$params) extends ..$parents {
          $newToString
          ..$body
        }
        """
      )
    }

    annottees.map(_.tree) toList match {
      // add support for case classes
      case (classDecl: ClassDef) :: Nil => modifiedDeclaration(classDecl)
      case _ => c.abort(c.enclosingPosition, "Invalid annottee")
    }
  }
}

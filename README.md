# macro-talk
Code and slides from OKC-FP talk about macros

import $ivy.`org.scalameta::scalameta:4.1.9`, scala.meta._

val func = """def helloWorld(x: Int): String = s"Hello: ${x}" """
val tree = func.parse[Stat].get

Term.Apply(
  Term.Name("function"),
  List(Term.Name("arg1"), Term.Name("arg2"))
) match {
  case q"$function(..$args)" =>
    println("1 " + function)
    println("2 " + args)
}

Term.Apply(
  Term.Name("rickyBobby"),
  List(Term.Name("arg1"), Term.Name("arg2"))
) match {
  case q"$function(..$args)" =>
    println("1 " + function)
    println("2 " + args)
}

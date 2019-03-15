/*
 * Copyright 2018-2019 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// package higherkindness.skeuomorph.mu

// import cats.Eq
// import qq.droste.Delay
// import higherkindness.skeuomorph.instances._
// import higherkindness.skeuomorph.uast.derivation._
// import org.typelevel.discipline.specs2.Discipline
// import cats.laws.discipline.{FoldableTests, FunctorTests, TraverseTests}
// import cats.implicits._
// import org.specs2._

// class MuTypeSpec extends Specification with ScalaCheck with Discipline {

//   def is = s2"""
//   $traverse
//   $functor
//   $foldable
//   """

//   implicit def muTypeEq[A: Eq](implicit d: Delay[Eq, Type]): Eq[Type[A]] = d.apply(Eq[A])

//   val traverse = checkAll("Traverse[mu.Type]", TraverseTests[Type].traverse[Int, Int, Int, Set[Int], Option, Option])
//   val functor  = checkAll("Functor[mu.Type]", FunctorTests[Type].functor[Int, Int, String])
//   val foldable = checkAll("Foldable[mu.Type]", FoldableTests[Type].foldable[Int, Int])
// }
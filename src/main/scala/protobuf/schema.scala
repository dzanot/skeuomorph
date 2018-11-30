/*
 * Copyright 2018 47 Degrees, LLC. <http://www.47deg.com>
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

package skeuomorph
package protobuf

import cats.Functor
import com.google.protobuf.descriptor.{DescriptorProto, FieldDescriptorProto}
import qq.droste.Coalgebra
import scalapb.descriptors.{FileDescriptor => _}

sealed trait ProtobufF[A]
object ProtobufF {
  final case class Field[A](name: String, tpe: A, position: Int, options: List[Option])
  final case class Option(name: String, value: String)

  final case class TDouble[A]()                extends ProtobufF[A]
  final case class TFloat[A]()                 extends ProtobufF[A]
  final case class TInt32[A]()                 extends ProtobufF[A]
  final case class TInt64[A]()                 extends ProtobufF[A]
  final case class TUint32[A]()                extends ProtobufF[A] // Json number
  final case class TUint64[A]()                extends ProtobufF[A] // Json number
  final case class TSint32[A]()                extends ProtobufF[A]
  final case class TSint64[A]()                extends ProtobufF[A]
  final case class TFixed32[A]()               extends ProtobufF[A]
  final case class TFixed64[A]()               extends ProtobufF[A]
  final case class TSfixed32[A]()              extends ProtobufF[A]
  final case class TSfixed64[A]()              extends ProtobufF[A]
  final case class TBool[A]()                  extends ProtobufF[A]
  final case class TString[A]()                extends ProtobufF[A]
  final case class TBytes[A]()                 extends ProtobufF[A]
  final case class TNamedType[A](name: String) extends ProtobufF[A]
  final case class TRequired[A](value: A)      extends ProtobufF[A]
  final case class TOptional[A](value: A)      extends ProtobufF[A]
  final case class TRepeated[A](value: A)      extends ProtobufF[A]
  final case class TEnum[A](
      name: String,
      symbols: List[(String, Int)],
      options: List[Option],
      aliases: List[(String, Int)])
      extends ProtobufF[A]
  final case class TMessage[A](name: String, fields: List[Field[A]], reserved: List[List[String]]) extends ProtobufF[A]

  implicit val protobufFunctor: Functor[ProtobufF] = new Functor[ProtobufF] {
    def map[A, B](fa: ProtobufF[A])(f: A => B): ProtobufF[B] = fa match {
      case TDouble()                              => TDouble()
      case TFloat()                               => TFloat()
      case TInt32()                               => TInt32()
      case TInt64()                               => TInt64()
      case TUint32()                              => TUint32()
      case TUint64()                              => TUint64()
      case TSint32()                              => TSint32()
      case TSint64()                              => TSint64()
      case TFixed32()                             => TFixed32()
      case TFixed64()                             => TFixed64()
      case TSfixed32()                            => TSfixed32()
      case TSfixed64()                            => TSfixed64()
      case TBool()                                => TBool()
      case TString()                              => TString()
      case TBytes()                               => TBytes()
      case TNamedType(name)                       => TNamedType(name)
      case TRequired(value)                       => TRequired(f(value))
      case TOptional(value)                       => TOptional(f(value))
      case TRepeated(value)                       => TRepeated(f(value))
      case TEnum(name, symbols, options, aliases) => TEnum(name, symbols, options, aliases)
      case TMessage(name, fields, reserved) =>
        TMessage(
          name,
          fields.map(field => field.copy(tpe = f(field.tpe))),
          reserved
        )
    }
  }

  // What I want is the parent type of everything... is that the DescriptorProto or something else?
  def fromProtobuf: Coalgebra[ProtobufF, (FieldDescriptorProto, DescriptorProto)] = Coalgebra{ case (fieldDescriptor: FieldDescriptorProto, _: DescriptorProto) =>
    // Need a match before the more granular type here to get at the enum descriptor
    fieldDescriptor.getType match {
      case FieldDescriptorProto.Type.TYPE_BOOL   => TBool()
      case FieldDescriptorProto.Type.TYPE_BYTES  => TBytes()
      case FieldDescriptorProto.Type.TYPE_DOUBLE => TDouble()
      case FieldDescriptorProto.Type.TYPE_ENUM => TEnum(fieldDescriptor.getName, ???, ???, ??? )
      case FieldDescriptorProto.Type.TYPE_FIXED32 => TFixed32()
      case FieldDescriptorProto.Type.TYPE_FIXED64 => TFixed64()
      case FieldDescriptorProto.Type.TYPE_FLOAT   => TFloat()
      case FieldDescriptorProto.Type.TYPE_GROUP => ??? // Is this supported???
      case FieldDescriptorProto.Type.TYPE_INT32 => TInt32()
      case FieldDescriptorProto.Type.TYPE_INT64 => TInt64()
      case FieldDescriptorProto.Type.TYPE_MESSAGE => ???
      case FieldDescriptorProto.Type.TYPE_SFIXED32 => TFixed32()
      case FieldDescriptorProto.Type.TYPE_SFIXED64 => TFixed64()
      case FieldDescriptorProto.Type.TYPE_SINT32   => TSint32()
      case FieldDescriptorProto.Type.TYPE_SINT64   => TSint64()
      case FieldDescriptorProto.Type.TYPE_STRING   => TString()
      case FieldDescriptorProto.Type.TYPE_UINT32   => TUint32()
      case FieldDescriptorProto.Type.TYPE_UINT64   => TUint64()
      case FieldDescriptorProto.Type.Unrecognized(x) => ???
    }
  }
}

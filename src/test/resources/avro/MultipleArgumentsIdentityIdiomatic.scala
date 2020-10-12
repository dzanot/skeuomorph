package foo.bar
import _root_.higherkindness.mu.rpc.protocol._
final case class Arg1(arg1: _root_.java.lang.String)
final case class Arg2(arg1: _root_.java.lang.String)
final case class Response(arg1: _root_.java.lang.String)
@service(Avro, Identity, namespace = Some("foo.bar"), methodNameStyle = Capitalize) trait MyGreeterService[F[_]] {
  def sayHelloAvro(arg1: _root_.foo.bar.Arg1, arg2: _root_.foo.bar.Arg2): F[_root_.foo.bar.Response]
}
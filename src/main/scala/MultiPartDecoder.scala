import unfiltered.request._
import unfiltered.response._
import unfiltered.netty._

import unfiltered.netty.request._

object MultiPartDecoder {

  def main(a: Array[String]) {

    Http(8080).handler(cycle.MultiPartDecoder{
      case POST(Path("/cycle/disk")) & MultiPart(req) => {
        case Decode(binding) =>
          val disk = MultiPartParams.Disk(binding)
          (disk.files("f"), disk.params("p")) match {
            case (Seq(f, _*), p) =>
              ResponseString(
                "cycle disk read file f named %s with content type %s and param p %s" format(
                  f.name, f.contentType, p))
              case _ =>  ResponseString("what's f?")
            }
      }
      case POST(Path("/cycle/stream") & MultiPart(req)) => {
        case Decode(binding) =>
          val stream = MultiPartParams.Streamed(binding)
          (stream.files("f"), stream.params("p")) match {
            case (Seq(f, _*), p) => ResponseString(
              "cycle stream read file f is named %s with content type %s and param p %s" format(
                f.name, f.contentType, p))
            case _ =>  ResponseString("what's f?")
          }
      }
      case r@POST(Path("/cycle/mem") & MultiPart(req)) => {
        case Decode(binding) =>
          val mem =  MultiPartParams.Memory(binding)
          (mem.files("f"), mem.params("p")) match {
            case (Seq(f, _*), p) => ResponseString(
              "cycle memory read file f is named %s with content type %s and param p %s" format(
                f.name, f.contentType, p))
            case _ =>  ResponseString("what's f?")
          }
      }
    }).handler(async.MultiPartDecoder{
      case r @ POST(Path("/async/disk")) & MultiPart(req) => {
        case Decode(binding) =>
          val disk = MultiPartParams.Disk(binding)
          (disk.files("f"), disk.params("p")) match {
            case (Seq(f, _*), p) =>
              binding.respond(ResponseString(
                "async disk read file f named %s with content type %s and param p %s" format(
                  f.name, f.contentType, p)))
              case _ =>  binding.respond(ResponseString("what's f?"))
            }
      }
      case r @ POST(Path("/async/stream") & MultiPart(req)) => {
        case Decode(binding) =>
          val stream = MultiPartParams.Streamed(binding)
          (stream.files("f"), stream.params("p")) match {
            case (Seq(f, _*), p) => binding.respond(ResponseString(
              "async stream read file f is named %s with content type %s and param p %s" format(
                f.name, f.contentType, p)))
            case _ =>  binding.respond(ResponseString("what's f?"))
          }
      }
      case r @ POST(Path("/async/mem") & MultiPart(req)) => {
        case Decode(binding) =>
          val mem = MultiPartParams.Memory(binding)
          (mem.files("f"), mem.params("p")) match {
            case (Seq(f, _*), p) => binding.respond(ResponseString(
              "async memory read file f is named %s with content type %s and param p %s" format(
                f.name, f.contentType, p)))
            case _ => binding.respond(ResponseString("what's f?"))
          }
      }
    }).handler(cycle.Planify{
      case Path("/") => Html(
        <html>
          <head><title>unfiltered file netty uploads test</title></head>
          <body>
            <h1>cycle disk upload</h1>
            <form action="/cycle/disk" method="POST" enctype="multipart/form-data">
              <input type="file" name="f" />
              <input type="text" name="p"/>
              <input type="submit"/>
            </form>

            <h1>cycle stream upload</h1>
            <form action="/cycle/stream" method="POST" enctype="multipart/form-data">
              <input type="file" name="f" />
              <input type="text" name="p"/>
              <input type="submit"/>
            </form>
            <h1>cycle mem upload</h1>
            <form action="/cycle/mem" method="POST" enctype="multipart/form-data">
              <input type="file" name="f" />
              <input type="text" name="p"/>
              <input type="submit"/>
            </form>
            <h1>async disk upload</h1>
            <form action="/async/disk" method="POST" enctype="multipart/form-data">
              <input type="file" name="f" />
              <input type="text" name="p"/>
              <input type="submit"/>
            </form>
            <h1>async stream upload</h1>
            <form action="/async/stream" method="POST" enctype="multipart/form-data">
              <input type="file" name="f" />
              <input type="text" name="p"/>
              <input type="submit"/>
            </form>
            <h1>async mem upload</h1>
            <form action="/async/mem" method="POST" enctype="multipart/form-data">
              <input type="file" name="f" />
              <input type="text" name="p"/>
              <input type="submit"/>
            </form>
          </body>
        </html>
      )
    }).run
  }
}

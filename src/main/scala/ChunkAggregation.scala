import unfiltered.request._
import unfiltered.response._
import unfiltered.netty._

import unfiltered.netty.request._

object ChunkAggregation {

  def main(a: Array[String]) {

    Http(8080).chunked().handler(cycle.Planify{
      case POST(Path("/cycle/disk")) & MultiPart(req) =>
        val disk = MultiPartParams.Disk(req)
        (disk.files("f"), disk.params("p")) match {
          case (Seq(f, _*), p) =>
            ResponseString(
              "cycle disk read file f named %s with content type %s and param p %s" format(
                f.name, f.contentType, p))
            case _ =>  ResponseString("what's f?")
          }
      case POST(Path("/cycle/stream") & MultiPart(req)) =>
        val stream = MultiPartParams.Streamed(req)
        (stream.files("f"), stream.params("p")) match {
          case (Seq(f, _*), p) => ResponseString(
            "cycle stream read file f is named %s with content type %s and param p %s" format(
              f.name, f.contentType, p))
          case _ =>  ResponseString("what's f?")
        }
      case r@POST(Path("/cycle/mem") & MultiPart(req)) =>
        val mem =  MultiPartParams.Memory(req)
        (mem.files("f"), mem.params("p")) match {
          case (Seq(f, _*), p) => ResponseString(
            "cycle memory read file f is named %s with content type %s and param p %s" format(
              f.name, f.contentType, p))
          case _ =>  ResponseString("what's f?")
        }
    }).handler(async.Planify{
      case r @ POST(Path("/async/disk")) & MultiPart(req) =>
        val disk = MultiPartParams.Disk(req)
        (disk.files("f"), disk.params("p")) match {
          case (Seq(f, _*), p) =>
            r.respond(ResponseString(
              "async disk read file f named %s with content type %s and param p" format(
                f.name, f.contentType, p)))
            case _ =>  r.respond(ResponseString("what's f?"))
          }
      case r @ POST(Path("/async/stream") & MultiPart(req)) =>
        val stream = MultiPartParams.Streamed(req)
        (stream.files("f"), stream.params("p")) match {
          case (Seq(f, _*), p) => r.respond(ResponseString(
            "async stream read file f is named %s with content type %s and param p %s" format(
              f.name, f.contentType, p)))
          case _ =>  r.respond(ResponseString("what's f?"))
        }
      case r @ POST(Path("/async/mem") & MultiPart(req)) =>
        val mem = MultiPartParams.Memory(req)
        (mem.files("f"), mem.params("p")) match {
          case (Seq(f, _*), p) => r.respond(ResponseString(
            "async memory read file f is named %s with content type %s and param p %s" format(
              f.name, f.contentType, p)))
          case _ => r.respond(ResponseString("what's f?"))
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

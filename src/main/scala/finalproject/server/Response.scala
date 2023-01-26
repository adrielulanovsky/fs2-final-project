package finalproject.server

import cats._

import java.nio.charset.StandardCharsets

/**
 * An http response.
 */
case class Response(
    httpVersion: String,
    status: Response.Status,
    body: Array[Byte],
    headers: Map[String, String]
) {
  /**
   * TODO #2
   *
   * Transforms the response into a valid byte representation as per HTTP specs.
   */
  def bytes: Array[Byte] = {
    val sb = new StringBuilder
    sb.append(s"$httpVersion ${status.code} ${status.reason}\r\n")
    headers.foreach { case (name, value) =>
      sb.append(s"$name: $value\r\n")
    }
    sb.append("\r\n")
    sb.append(new String(body, StandardCharsets.UTF_8))
    sb.toString.getBytes(StandardCharsets.UTF_8)
  }

  /**
   * String representation of this response (mostly for debugging).
   */
  override def toString: String = Response.show.show(this)
}

object Response {
  /**
   * ADT (incomplete) for valid http status.
   */
  abstract class Status(val code: Int, val reason: String)
  object Ok extends Status(200, "OK")
  object NotFound extends Status(404, "Not Found")
  object InternalServerError extends Status(500, "Internal Server Error")

  implicit val show: Show[Response] = Show.show { response =>
    s"""Response(
       |  httpVersion = ${response.httpVersion}
       |  status = ${response.status.code} ${response.status.reason}
       |  headers = [
       |    ${response.headers.map { case (k, v) => s"$k: $v" }.mkString("\n    ")}
       |  ]
       |  body = ${new String(response.body)}
       |)""".stripMargin
  }
}
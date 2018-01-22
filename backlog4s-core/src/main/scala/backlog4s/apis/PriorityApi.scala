package backlog4s.apis

import backlog4s.datas.{Credentials, Priority, Resolution}
import backlog4s.dsl.ApiDsl.ApiPrg
import backlog4s.dsl.HttpADT.Response
import backlog4s.dsl.HttpQuery
import backlog4s.formatters.SprayJsonFormats._

class PriorityApi(override val baseUrl: String,
                  override val credentials: Credentials) extends Api {

  import backlog4s.dsl.ApiDsl.HttpOp._

  val resource = "priorities"

  def all: ApiPrg[Response[Seq[Priority]]] =
    get[Seq[Priority]](
      HttpQuery(
        path = resource,
        credentials = credentials,
        baseUrl = baseUrl
      )
    )
}

object PriorityApi extends ApiContext[PriorityApi] {
  override def apply(baseUrl: String, credentials: Credentials): PriorityApi =
    new PriorityApi(baseUrl, credentials)
}
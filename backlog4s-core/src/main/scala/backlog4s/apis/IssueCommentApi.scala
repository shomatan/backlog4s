package backlog4s.apis

import backlog4s.datas.CustomForm.CustomForm
import backlog4s.datas.Order.Order
import backlog4s.datas._
import backlog4s.dsl.ApiDsl.ApiPrg
import backlog4s.dsl.HttpADT.Response
import backlog4s.dsl.HttpQuery
import backlog4s.formatters.SprayJsonFormats._
import backlog4s.utils.QueryParameter

object IssueCommentApi {
  import backlog4s.dsl.ApiDsl.HttpOp._

  def resource(issueIdOrKey: IdOrKeyParam[Issue]): String =
    s"issues/$issueIdOrKey/comments"

  def allOf(issueIdOrKey: IdOrKeyParam[Issue],
            minId: Option[Id[Comment]] = None,
            maxId: Option[Id[Comment]] = None,
            count: Long = 20,
            order: Order = Order.Desc): ApiPrg[Response[Seq[Comment]]] = {
    val params = Map(
      "minId" -> minId.map(_.value.toString).getOrElse(""),
      "maxId" -> maxId.map(_.value.toString).getOrElse(""),
      "count" -> count.toString,
      "order" -> order.toString
    )

    get[Seq[Comment]](
      HttpQuery(
        resource(issueIdOrKey),
        QueryParameter.removeEmptyValue(params)
      )
    )
  }

  def count(issueIdOrKey: IdOrKeyParam[Issue]): ApiPrg[Response[Count]] =
    get[Count](
      HttpQuery(s"${resource(issueIdOrKey)}/count)")
    )

  def getById(issueIdOrKey: IdOrKeyParam[Issue], id: Id[Comment]): ApiPrg[Response[Comment]] =
    get[Comment](
      HttpQuery(s"${resource(issueIdOrKey)}/${id.value}")
    )

  def add(issueIdOrKey: IdOrKeyParam[Issue], form: AddCommentForm): ApiPrg[Response[Comment]] =
    post[AddCommentForm, Comment](
      HttpQuery(resource(issueIdOrKey)),
      form
    )

  def update(issueIdOrKey: IdOrKeyParam[Issue],
             id: Id[Comment],
             newContent: String): ApiPrg[Response[Comment]] =
    put[CustomForm, Comment](
      HttpQuery(s"${resource(issueIdOrKey)}/${id.value}"),
      Map(
        "content" -> newContent
      )
    )

  def notifications(issueIdOrKey: IdOrKeyParam[Issue], id: Id[Comment]): ApiPrg[Response[Seq[Notification]]] =
    get[Seq[Notification]](
      HttpQuery(s"${resource(issueIdOrKey)}/${id.value}/notifications")
    )

  def addNotification(issueIdOrKey: IdOrKeyParam[Issue],
                      id: Id[Comment],
                      form: AddNotificationForm): ApiPrg[Response[Comment]] =
    post[AddNotificationForm, Comment](
      HttpQuery(
        s"${resource(issueIdOrKey)}/${id.value}/notifications"
      ),
      form
    )
}
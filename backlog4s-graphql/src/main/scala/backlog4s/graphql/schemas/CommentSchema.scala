package backlog4s.graphql.schemas

import backlog4s.datas._
import backlog4s.graphql.repositories.BacklogRepository
import sangria.schema._

object CommentSchema extends BacklogSchema[BacklogRepository, Comment] {

  val star: ObjectType[Unit, Star] =
    ObjectType(
      "Star",
      "Backlog star",
      () => fields[Unit, Star](
        Field(
          "id",
          IntType,
          resolve = _.value.id.value.toInt
        ),
        Field(
          "comment",
          OptionType(StringType),
          resolve = _.value.comment
        ),
        Field(
          "url",
          StringType,
          resolve = _.value.url
        ),
        Field(
          "title",
          StringType,
          resolve = _.value.title
        ),
        Field(
          "presenter",
          UserSchema.schema,
          resolve = _.value.presenter
        ),
        Field(
          "created",
          StringType,
          resolve = _.value.created.toString
        )
      )
    )

  val notification: ObjectType[Unit, Notification] =
    ObjectType(
      "Notification",
      "Backlog notification",
      () => fields[Unit, Notification](
        Field(
          "id",
          IntType,
          resolve = _.value.id.value.toInt
        ),
        Field(
          "alreadyRead",
          BooleanType,
          resolve = _.value.alreadyRead
        ),
        Field(
          "reason",
          IntType,
          resolve = _.value.reason
        ),
        Field(
          "user",
          UserSchema.schema,
          resolve = _.value.user
        ),
        Field(
          "resourceAlreadyRead",
          BooleanType,
          resolve = _.value.resourceAlreadyRead
        )
      )
    )

  val schema: ObjectType[BacklogRepository, Comment] =
    ObjectType(
      "Comment",
      "Issue comment",
      () => fields[BacklogRepository, Comment](
        Field(
          "id",
          IntType,
          resolve = _.value.id.value.toInt
        ),
        Field(
          "content",
          StringType,
          resolve = _.value.content
        ),
        Field(
          "createdUser",
          UserSchema.schema,
          resolve = _.value.createdUser
        ),
        Field(
          "created",
          StringType,
          resolve = _.value.created.toString()
        ),
        Field(
          "updated",
          OptionType(StringType),
          resolve = _.value.updated.map(_.toString())
        ),
        Field(
          "stars",
          ListType(star),
          resolve = _.value.stars
        ),
        Field(
          "notifications",
          ListType(notification),
          resolve = _.value.notifications
        )
      )
    )
}

package com.github.chaabaj.backlog4s.graphql.repositories

import com.github.chaabaj.backlog4s.datas._
import com.github.chaabaj.backlog4s.dsl.BacklogHttpInterpret
import sangria.execution.deferred._

import scala.concurrent.{ExecutionContext, Future}



class BacklogFetchers(interpret: BacklogHttpInterpret[Future]) {

  implicit val hasWikiId: HasId[Wiki, Long] = wiki => wiki.id.value
  implicit val hasProjectId: HasId[Project, Long] = project => project.id.value
  implicit val hasIssueId: HasId[Issue, Long] = issue => issue.id.value

  val projects = Fetcher(
    (repository: BacklogRepository, ids: Seq[Long]) =>
      repository.getProjects(ids.map(id => ProjectT.id(id.toInt)))
  )

  val wikis = Fetcher(
    (repository: BacklogRepository, ids: Seq[Long]) =>
      repository.getWikis(ids.map(id => WikiT.id(id.toInt)))
  )
}

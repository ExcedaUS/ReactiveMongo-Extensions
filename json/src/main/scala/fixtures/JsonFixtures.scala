// Copyright (C) 2014 Fehmi Can Saglam (@fehmicans) and contributors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package reactivemongo.extensions.json.fixtures

import play.api.libs.json.{ JsObject, Json }
import play.modules.reactivemongo.json._
import reactivemongo.api.DB
import reactivemongo.api.commands.WriteResult
import reactivemongo.extensions.fixtures.Fixtures
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ ExecutionContext, Future }

class JsonFixtures(db: => DB)(implicit ec: ExecutionContext) extends Fixtures[JsObject] {

  def map(document: JsObject): JsObject = document

  def bulkInsert(collectionName: String, documents: Stream[JsObject]): Future[Int] = db.collection[JSONCollection](
    collectionName).bulkInsert(documents, ordered = true).map(_.n)

  def removeAll(collectionName: String): Future[WriteResult] =
    db.collection[JSONCollection](collectionName).
      remove(selector = Json.obj(), firstMatchOnly = false)

  def drop(collectionName: String): Future[Boolean] =
    db.collection[JSONCollection](collectionName).drop(failIfNotFound = false)

}

object JsonFixtures {
  def apply(db: DB)(implicit ec: ExecutionContext): JsonFixtures = new JsonFixtures(db)
}


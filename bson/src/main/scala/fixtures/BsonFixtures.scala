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

package reactivemongo.extensions.fixtures

import play.api.libs.json.JsObject
import reactivemongo.api.{ DB, DefaultDB }
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.BSONFormats

import scala.concurrent.{ ExecutionContext, Future }

class BsonFixtures(db: => Future[DefaultDB])(implicit ec: ExecutionContext) extends Fixtures[BSONDocument] {
  def map(document: JsObject): BSONDocument =
    BSONFormats.BSONDocumentFormat.reads(document).get

  def bulkInsert(collectionName: String, documents: Stream[BSONDocument]): Future[Int] = db.flatMap(_.collection[BSONCollection](
    collectionName).bulkInsert(documents, ordered = true).map(_.n))

  def removeAll(collectionName: String): Future[WriteResult] =
    db.flatMap(_.collection[BSONCollection](collectionName).
      remove(selector = BSONDocument.empty, firstMatchOnly = false))

  def drop(collectionName: String): Future[Boolean] =
    db.flatMap(_.collection[BSONCollection](collectionName).drop(failIfNotFound = false))

}

object BsonFixtures {
  def apply(db: Future[DefaultDB])(implicit ec: ExecutionContext): BsonFixtures =
    new BsonFixtures(db)
}


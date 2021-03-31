package u05lab.code


import u05lab.code.Kind.Kind

import scala.collection.mutable

/* See: https://bitbucket.org/mviroli/oop2018-esami/src/master/a01b/e1/Test.java */


object Kind extends Enumeration {
  type Kind = Value;
  val RETIRED = Value(0, "RETIRED")
  val FAILED = Value(1, "FAILED")
  val SUCCEEDED = Value(2, "SUCCEEDED")
}

/*
trait for Exam Result
 */
sealed trait ExamResult {
  def getKind: Kind

  def getEvaluation: Option[Integer]

  def cumLaude: Boolean
}

/*
 * Implements methods of ExamResult
 */
object ExamResult {

  private case class ExamResultImpl(kind: Kind) extends ExamResult {
    override def getKind: Kind = kind

    override def getEvaluation: Option[Integer] = Option.empty

    override def cumLaude: Boolean = false

    override def toString: String = kind.toString
  }

  private case class ExamResultSuccImpl(ev: Int) extends ExamResult {
    require(ev > 18 && ev <= 30)

    override def getKind: Kind = Kind.SUCCEEDED

    override def getEvaluation: Option[Integer] = Option(ev)

    override def cumLaude: Boolean = false

    override def toString: String = Kind.SUCCEEDED.toString + "(" + ev + ")"
  }

  private case class ExamResultLaudeImpl() extends ExamResult {
    override def getKind: Kind = Kind.SUCCEEDED

    override def getEvaluation: Option[Integer] = Option(30)

    override def cumLaude: Boolean = true

    override def toString: String = Kind.SUCCEEDED.toString + "(30L)"
  }

  def failed: ExamResult = ExamResultImpl(Kind.FAILED)

  def retired: ExamResult = ExamResultImpl(Kind.RETIRED)

  def succeeded(evaluation: Int): ExamResult = ExamResultSuccImpl(evaluation)

  def succeededCumLaude: ExamResult = ExamResultLaudeImpl()
}

/*
trait for Exams Manager
 */
sealed trait ExamsManager {
  def createNewCall(call: String): Unit

  def addStudentResult(call: String, student: String, result: ExamResult): Unit

  def getAllStudentsFromCall(call: String): collection.Set[String]

  def getEvaluationsMapFromCall(call: String): collection.Map[String, Integer]

  def getResultsMapFromStudent(student: String): collection.Map[String, String]

  def getBestResultFromStudent(student: String): Option[Integer]
}


/*
 * Implements methods of ExamsManager
 */
object ExamsManager {

  def apply(): ExamsManager = ExamsManagerImpl()

  private case class ExamsManagerImpl() extends ExamsManager {
    private val mutableMap: mutable.Map[String, mutable.Map[String, ExamResult]] = mutable.HashMap()

    def checkArgument[A](arg: Boolean): Any = if (arg) throw new IllegalArgumentException

    override def addStudentResult(call: String, student: String, result: ExamResult): Unit = {
      checkArgument(!mutableMap.contains(call))
      checkArgument(mutableMap(call).contains(student))
      mutableMap(call).put(student, result)
    }

    override def createNewCall(call: String): Unit = {
      checkArgument(mutableMap.contains(call))
      mutableMap.put(call, new mutable.HashMap[String, ExamResult])
    }

    override def getAllStudentsFromCall(call: String): collection.Set[String] = {
      checkArgument(!mutableMap.contains(call))
      mutableMap(call).keySet
    }

    override def getEvaluationsMapFromCall(call: String): Map[String, Integer] = {
      checkArgument(!mutableMap.contains(call))
      mutableMap(call).toMap
        .filter(e => e._2.getEvaluation.isDefined)
        .map(e => (e._1, e._2.getEvaluation.get))
    }

    override def getResultsMapFromStudent(student: String): Map[String, String] = {
      mutableMap.filter(e => e._2.contains(student)) //tutti i risultati degli studenti
        .filter(e => e._2.get(student).isDefined)
        .map(e => e._1 -> e._2.get(student).get.toString)
        .toMap
    }

    override def getBestResultFromStudent(student: String): Option[Integer] = {
      mutableMap.filter(e => e._2.contains(student))
        .filter(e => e._2(student).getEvaluation.isDefined)
        .map(e => e._2(student).getEvaluation)
        .max
    }
  }

}
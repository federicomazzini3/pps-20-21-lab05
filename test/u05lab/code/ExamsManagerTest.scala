package u05lab.code

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions._

import java.util
import java.util.{Arrays, HashSet, Optional}

class ExamsManagerTest { // verifica base di ExamResultFactory

  private val em = ExamsManager()

  // verifica base di ExamResultFactory
  @org.junit.Test def testExamResultsBasicBehaviour(): Unit = { // esame fallito, non c'è voto
    assertEquals(ExamResult.failed.getKind, Kind.FAILED)
    assertFalse(ExamResult.failed.getEvaluation.isDefined)
    assertFalse(ExamResult.failed.cumLaude)
    assertEquals(ExamResult.failed.toString, "FAILED")
    // lo studente si è ritirato, non c'è voto
    assertEquals(ExamResult.retired.getKind, Kind.RETIRED)
    assertFalse(ExamResult.retired.getEvaluation.isDefined)
    assertFalse(ExamResult.retired.cumLaude)
    assertEquals(ExamResult.retired.toString, "RETIRED")
    // 30L
    assertEquals(ExamResult.succeededCumLaude.getKind, Kind.SUCCEEDED)
    assertEquals(ExamResult.succeededCumLaude.getEvaluation, Option(30))
    assertTrue(ExamResult.succeededCumLaude.cumLaude)
    assertEquals(ExamResult.succeededCumLaude.toString, "SUCCEEDED(30L)")
    // esame superato, ma non con lode
    assertEquals(ExamResult.succeeded(28).getKind, Kind.SUCCEEDED)
    assertEquals(ExamResult.succeeded(28).getEvaluation, Option(28))
    assertFalse(ExamResult.succeeded(28).cumLaude)
    assertEquals(ExamResult.succeeded(28).toString, "SUCCEEDED(28)")
  }

  // verifica eccezione in ExamResultFactory
  @org.junit.Test(expected = classOf[IllegalArgumentException]) def optionalTestEvaluationCantBeGreaterThan30(): Unit = {
    ExamResult.succeeded(32)
  }

  @org.junit.Test(expected = classOf[IllegalArgumentException]) def optionalTestEvaluationCantBeSmallerThan18(): Unit = {
    ExamResult.succeeded(17)
  }

  // metodo di creazione di una situazione di risultati in 3 appelli
  private def prepareExams(): Unit = {
    em.createNewCall("gennaio")
    em.createNewCall("febbraio")
    em.createNewCall("marzo")
    em.addStudentResult("gennaio", "rossi", ExamResult.failed) // rossi -> fallito

    em.addStudentResult("gennaio", "bianchi", ExamResult.retired) // bianchi -> ritirato

    em.addStudentResult("gennaio", "verdi", ExamResult.succeeded(28)) // verdi -> 28

    em.addStudentResult("gennaio", "neri", ExamResult.succeededCumLaude) // neri -> 30L

    em.addStudentResult("febbraio", "rossi", ExamResult.failed) // etc..

    em.addStudentResult("febbraio", "bianchi", ExamResult.succeeded(20))
    em.addStudentResult("febbraio", "verdi", ExamResult.succeeded(30))
    em.addStudentResult("marzo", "rossi", ExamResult.succeeded(25))
    em.addStudentResult("marzo", "bianchi", ExamResult.succeeded(25))
    em.addStudentResult("marzo", "viola", ExamResult.failed)
  }

  // verifica base della parte obbligatoria di ExamManager
  @org.junit.Test def testExamsManagement(): Unit = {
    this.prepareExams()
    // partecipanti agli appelli di gennaio e marzo
    assertEquals(em.getAllStudentsFromCall("gennaio"), collection.mutable.Set[String]("rossi", "bianchi", "verdi", "neri"))
    assertEquals(em.getAllStudentsFromCall("marzo"), collection.mutable.Set[String]("rossi", "bianchi", "viola"))
    // promossi di gennaio con voto
    assertEquals(em.getEvaluationsMapFromCall("gennaio").size, 2)
    assertEquals(em.getEvaluationsMapFromCall("gennaio").get("verdi").get, 28)
    assertEquals(em.getEvaluationsMapFromCall("gennaio").get("neri").get, 30)
    // promossi di febbraio con voto
    assertEquals(em.getEvaluationsMapFromCall("febbraio").size, 2)
    assertEquals(em.getEvaluationsMapFromCall("febbraio").get("bianchi").get, 20)
    assertEquals(em.getEvaluationsMapFromCall("febbraio").get("verdi").get, 30)
    // tutti i risultati di rossi (attenzione ai toString!!)
    assertEquals(em.getResultsMapFromStudent("rossi").size, 3)
    assertEquals(em.getResultsMapFromStudent("rossi").get("gennaio").get, "FAILED")
    assertEquals(em.getResultsMapFromStudent("rossi").get("febbraio").get, "FAILED")
    assertEquals(em.getResultsMapFromStudent("rossi").get("marzo").get, "SUCCEEDED(25)")
    // tutti i risultati di bianchi
    assertEquals(em.getResultsMapFromStudent("bianchi").size, 3)
    assertEquals(em.getResultsMapFromStudent("bianchi").get("gennaio").get, "RETIRED")
    assertEquals(em.getResultsMapFromStudent("bianchi").get("febbraio").get, "SUCCEEDED(20)")
    assertEquals(em.getResultsMapFromStudent("bianchi").get("marzo").get, "SUCCEEDED(25)")
    // tutti i risultati di neri
    assertEquals(em.getResultsMapFromStudent("neri").size, 1)
    assertEquals(em.getResultsMapFromStudent("neri").get("gennaio").get, "SUCCEEDED(30L)")
  }

  // verifica del metodo ExamManager.getBestResultFromStudent
  @org.junit.Test def optionalTestExamsManagement(): Unit = {
    this.prepareExams()
    // miglior voto acquisito da ogni studente, o vuoto..
    assertEquals(em.getBestResultFromStudent("rossi"), Option(25))
    assertEquals(em.getBestResultFromStudent("bianchi"), Option(25))
    assertEquals(em.getBestResultFromStudent("neri"), Option(30))
  }


  @org.junit.Test(expected = classOf[IllegalArgumentException]) def optionalTestCantCreateACallTwice(): Unit = {
    this.prepareExams()
    em.createNewCall("marzo")
  }

  @org.junit.Test(expected = classOf[IllegalArgumentException]) def optionalTestCantRegisterAnEvaluationTwice(): Unit = {
    this.prepareExams()
    em.addStudentResult("gennaio", "verdi", ExamResult.failed)
  }

}
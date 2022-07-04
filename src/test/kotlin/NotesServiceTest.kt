import org.junit.Test

import org.junit.Assert.*

class NotesServiceTest {

    @Test
    fun getNotes() {

    }

    @Test
    fun setNotes() {
    }

    @Test
    fun addSuccessful() {
        val service : NotesService = NotesService ()
        val expectedResult : Int = 1
        assertEquals(expectedResult, service.add(0, "title", "Text", 1))
    }

    @Test
    fun addFailed() {
        val service : NotesService = NotesService ()
        val expectedResult : Int = 5
        assertNotEquals(expectedResult, service.add(0, "title", "Text", 1))
    }

    @Test
    fun deleteSuccessful() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 1)
        val expectedResult = true
        assertEquals(expectedResult, service.delete(1))
    }

    @Test (expected = NotesNotFoundException::class)
    fun deleteFailedNotFoundId() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 1)
       service.delete(11)
    }

    @Test
    fun deleteSuccessfulSecondVariant() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 1)
        val expectedResult = true
        service.delete(1)
        val result = service.notes[1]?.deleted ?: false
        assertEquals(expectedResult, result)
    }
    @Test
    fun getByIdSuccessful() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        val expectedResult = Notes (2,  "title2", "Text2", 1)
        assertEquals(expectedResult, service.getById(2))
    }

    @Test(expected = NotesNotFoundException::class)
    fun getByIdNotFoundId() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
       service.getById(25)
    }

    @Test(expected = NotesNotFoundException::class)
    fun editNotFoundNoteException() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.edit(Notes(25,"title2", "Text2", 1) )
    }

    @Test(expected = NoteIsDeleted::class)
    fun editNoteIsDeletedException() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 1)
        service.delete(1)
        service.edit(Notes(1,"title2", "Text2", 1) )
    }

    @Test
    fun editSuccessful() {
        val service : NotesService = NotesService ()
        val expectedResult = true
        service.add(0, "title", "Text", 1)
        val newNote = Notes(1,"title2", "Text2", 1)
        service.edit(newNote)
        assertEquals(expectedResult, service.edit(newNote))
    }

    @Test
    fun editSuccessfulSecondVariantTextComparison() {
        val service : NotesService = NotesService ()
        val expectedResult = "Text2"
        service.add(0, "title", "Text", 1)
        val newNote = Notes(1,"title2", "Text2", 1)
        service.edit(newNote)
        val result = service.notes[1]?.text
        assertEquals(expectedResult, result)
    }

    @Test
    fun editSuccessfulSecondVariantTitleComparison() {
        val service : NotesService = NotesService ()
        val expectedResult = "title2"
        service.add(0, "title", "Text", 1)
        val newNote = Notes(1,"title2", "Text2", 1)
        service.edit(newNote)
        val result = service.notes[1]?.title
        assertEquals(expectedResult, result)
    }
    @Test
    fun getSuccessful() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 2)
        service.add(0, "title2", "Text2", 1)
        service.add(0, "title3", "Text3", 2)
        val expectedResult= mutableListOf<Notes>(Notes(1, "title", "Text", 2), Notes (3, "title3", "Text3", 2))
        assertEquals(expectedResult, service.get(2))
    }

    @Test
    fun getEmptyListNotFoundAuthor() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 2)
        service.add(0, "title2", "Text2", 1)
        service.add(0, "title3", "Text3", 2)
        val expectedResult= mutableListOf<Notes>()
        assertEquals(expectedResult, service.get(4))
    }

    @Test(expected = NotesNotFoundException::class)
    fun createCommentNoteNotFound() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.createComment(24,"Text")
    }

    @Test(expected = NoteIsDeleted::class)
    fun createCommentNotIsDeleted() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.delete(2)
        service.createComment(2,"Text")
    }

    @Test
    fun createCommentSuccessful() {
        val service : NotesService = NotesService ()
        val expectedResult = 1
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        assertEquals(expectedResult,service.createComment(2,"Text"))
    }

    @Test
    fun createCommentSuccessfulSecondVariant() {
        val service : NotesService = NotesService ()
        val expectedResult = mutableListOf(Comments(1,"Text"))
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.createComment(2,"Text")
        val result = service.notes[2]?.comments
        assertEquals(expectedResult,result)
    }


    @Test (expected = NotesNotFoundException::class)
    fun deleteCommentNotesNotFound() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.createComment(2,"Text")
        service.deleteComment(1,3)
    }

    @Test (expected = NoteIsDeleted::class)
    fun deleteCommentNotesIsDeleted() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.delete(2)
        service.deleteComment(1,2)
    }

    @Test
    fun deleteCommentSuccessful() {
        val service : NotesService = NotesService ()
        val expectedResult = true
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.createComment(2,"Text")
        assertEquals(expectedResult,service.deleteComment(1,2) )
    }

    @Test
    fun deleteCommentFailed() {
        val service : NotesService = NotesService ()
        val expectedResult = false
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.createComment(2,"Text")
        assertEquals(expectedResult,service.deleteComment(1,1) )
    }

    @Test(expected = NotesNotFoundException::class)
    fun editCommentNotesNotFound() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.createComment(2,"Text")
        service.editComment(3,1, "newText")
    }

    @Test(expected = NoteIsDeleted::class)
    fun editCommentNoteIsDeleted() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.createComment(2,"Text")
        service.delete(2)
        service.editComment(2,1, "newText")
    }

    @Test(expected = CommentIsDeleted::class)
    fun editCommentCommentIsDeleted() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.createComment(2,"Text")
        service.deleteComment(1, 2)
        service.editComment(2,1, "newText")
    }

    @Test
    fun editCommentSuccessful() {
        val service : NotesService = NotesService ()
        val expectedResult = true
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.createComment(2,"Text")
        assertEquals(expectedResult,  service.editComment(2,1, "newText"))
    }

    @Test
    fun editCommentSuccessfulCommentComparison() {
        val service : NotesService = NotesService ()
        val expectedResult = mutableListOf(Comments(1,"newText"))
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.createComment(2,"Text")
        service.editComment(2,1, "newText")
        val result = service.notes[2]?.comments
        assertEquals(expectedResult, result)
    }

    @Test (expected = NotesNotFoundException::class)
    fun restoreCommentNotesNotFound() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.createComment(2,"Text")
        service.restoreComment(1,3)
    }

    @Test (expected = NoteIsDeleted::class)
    fun restoreCommentNotesIsDeleted() {
        val service : NotesService = NotesService ()
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.delete(2)
        service.restoreComment(1,2)
    }

    @Test
    fun restoreCommentSuccessful() {
        val service : NotesService = NotesService ()
        val expectedResult = true
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.createComment(2,"Text")
        service.deleteComment(1,2)
        assertEquals(expectedResult,service.restoreComment(1,2) )
    }

    @Test
    fun restoreCommentFailed() {
        val service : NotesService = NotesService ()
        val expectedResult = false
        service.add(0, "title", "Text", 1)
        service.add(0, "title2", "Text2", 1)
        service.createComment(2,"Text")
        assertEquals(expectedResult,service.restoreComment(1,1) )
    }

}
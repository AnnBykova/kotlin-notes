

data class Notes (
    val nid: Int,
    val title: String,
    val text: String,
    val ownerId: Int,
    val deleted: Boolean = false,
    val comments : MutableList <Comments> = mutableListOf<Comments>()
        )

data class Comments (
    val cid: Int,
    val text: String,
    val deleted: Boolean = false
)
interface Service <T> {
    fun setId(): Int
    fun delete (id : Int): Boolean
    fun getById (id: Int): T
    fun edit (newNote: Notes): Boolean

}
class NotesService () : Service <Notes> {
    var notes = mutableMapOf<Int, Notes>()
    private var counterNotesId: Int = 0
    private var counterCommentsId: Int = 0
    override fun setId(): Int {
        counterNotesId += 1
        return counterNotesId
    }

    fun add(nid: Int,  title: String,text: String, ownerId: Int): Int {
        val nid: Int = setId()
        notes.put(nid, Notes(nid, title, text, ownerId))
        return nid
    }

    override fun delete(id: Int): Boolean {
        val note = notes[id]?: throw NotesNotFoundException("заметка с id=$id не найдена")
        notes[id]=note.copy(deleted = true)
        return true
    }

    override fun getById(id: Int): Notes {
        return notes[id]?: throw NotesNotFoundException("заметка с id=$id не найдена")
    }

    override fun edit(newNote: Notes): Boolean {
        val note = notes[newNote.nid]?: throw NotesNotFoundException("заметка с id=$newNote.nid не найдена")
        if (!note.deleted) {
            notes[newNote.nid] = note.copy(text = newNote.text, title = newNote.title)
            return true
        }
           throw NoteIsDeleted("заметка удалена")
    }

    fun get(ownerId: Int): List<Notes> {
        val userNotes = mutableListOf<Notes>()
        for (note in notes) {
            if (note.value.ownerId == ownerId) {
                userNotes.add(note.value)
            }
        }
        return userNotes
    }

    fun setCommentsId(): Int {
        counterCommentsId++
        return counterCommentsId
    }

    fun createComment( noteId: Int, text: String): Int {
        val note = notes[noteId]?: throw NotesNotFoundException("заметка с id=$noteId не найдена")
        if (!note.deleted) {
                val cid: Int = setCommentsId()
                note.comments.add(Comments(cid, text))
                notes[noteId] = note
                return cid
            }
        throw NoteIsDeleted ("заметка удалена")
        }

    fun deleteComment (cid: Int, noteId: Int): Boolean{
        val note = notes[noteId]?:throw NotesNotFoundException("заметка с id=$noteId не найдена")
        var result = false
        val commentsOfNote = note.comments
        if (!note.deleted) {
            for ((index,value) in commentsOfNote.withIndex()) {
               if (value.cid == cid) {
               commentsOfNote[index]=value.copy(deleted=true)
                   result= true
                   break
            }
            }
            return result
        }
        throw NoteIsDeleted ("заметка удалена")
    }

    fun editComment ( noteId: Int,cid: Int, newText: String): Boolean{
        val note = notes[noteId]?:throw NotesNotFoundException("заметка с id=$noteId не найдена")
        val commentsOfNote = note.comments
        var result = false
        if (!note.deleted) {
            for ((index, value) in commentsOfNote.withIndex()) {
                if (value.cid == cid) {
                    when (value.deleted) {
                        false -> {
                            commentsOfNote[index] = value.copy(text = newText)
                            result= true
                            break
                        }
                        true -> {
                            throw CommentIsDeleted("комментарий удален")
                        }
                    }
                }
            }
            return result
        }
        throw NoteIsDeleted ("заметка удалена")
    }

    fun restoreComment (cid: Int, noteId: Int): Boolean{
        val note = notes[noteId]?:throw NotesNotFoundException("заметка с id=$noteId не найдена")
        val commentsOfNote = note.comments
        var result = false
        if (!note.deleted) {
            for ((index, value) in commentsOfNote.withIndex()) {
                if (value.cid == cid) {
                    if  (value.deleted) {
                            commentsOfNote[index] = value.copy(deleted = false)
                            result = true
                        break
                        }
                        }
                    }
            return result
                }
        throw NoteIsDeleted ("заметка удалена")
    }
    }





fun main (){
    val service : NotesService = NotesService ()
    service.add(0, "title", "Text", 1)
    service.add(0, "title2", "Text2", 1)
    service.add(0, "title3", "Text3", 1)
   println (service.add(0, "title0", "Text", 1))
    println(service.delete(1))
    service.delete(2)
    println(service.notes[2]?.deleted ?: "pfxtv&")
    println(service.getById(3))
    println(service.getById(1))
    println(service.edit(Notes(3,"title new","Text new",1)))
    println(service.getById(3))
    //println(service.getById(13))
    service.createComment(3,"com1")
    service.createComment(3,"com2")
    service.createComment(3,"com3")
    println(service.getById(3))
    service.editComment(1,3,"new com")
    println(service.getById(3))
    service.deleteComment(3,3)
    println(service.getById(3))
    service.restoreComment(3,3)
    println(service.getById(3))
    service.restoreComment(3,3)
    println(service.getById(3))
}

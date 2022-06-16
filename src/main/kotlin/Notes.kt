

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

    fun add(nid: Int, text: String, title: String, ownerId: Int): Int {
        val nid: Int = setId()
        notes.put(nid, Notes(nid, title, text, ownerId))
        return nid
    }

    override fun delete(id: Int): Boolean {
        if (notes.containsKey(id)) {
            notes[id] = notes.get(id)!!.copy(deleted = true)
            return true
        }
        throw NotesNotFoundException("заметка с id=$id не найдена")
        return false
    }

    override fun getById(id: Int): Notes {
        if (notes.containsKey(id)) {
            return notes[id]!!
        }
        throw NotesNotFoundException("заметка с id=$id не найдена")
    }

    override fun edit(newNote: Notes): Boolean {

        if (notes.containsKey(newNote.nid)) {
            val id = newNote.nid
            if (notes.get(id)!!.deleted == false) {
                notes[newNote.nid] = notes.get(newNote.nid)!!.copy(text = newNote.text, title = newNote.title)
                return true
            }
            throw NoteIsDeleted("заметка удалена")
            return false
        }
        throw NotesNotFoundException("заметка с id=$newNote.nid не найдена")
        return false
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

    fun createComment(cid: Int, noteId: Int, text: String): Int {
        if (notes[noteId]?.deleted== false) {
                val cid: Int = setCommentsId()
                notes[noteId]?.comments?.add(Comments(cid, text))
                return cid
            }
        throw NoteIsDeleted ("заметка удалена")
        }

    fun deleteComment (cid: Int, noteId: Int): Boolean{
        val note = notes[noteId]
        val commentsOfNote = note!!.comments
        if (note!!.deleted== false) {
            for ((index,value) in commentsOfNote.withIndex()) {
               if (value.cid == cid) {
               commentsOfNote[index]=value.copy(deleted=true)
                   break
            }
            return true}
        }
        throw NoteIsDeleted ("заметка удалена")
    }

    fun editComment (cid: Int, noteId: Int, newText: String): Boolean{
        val note = notes[noteId]
        val commentsOfNote = note!!.comments
        if (note!!.deleted== false) {
            for ((index, value) in commentsOfNote.withIndex()) {
                if (value.cid == cid) when (value.deleted) {
                    false -> {
                        commentsOfNote[index] = value.copy(text = newText)
                        return true
                    }
                    true -> {
                        throw CommentIsDeleted("комментарий удален")
                        return false
                    }
                }
            }
        }
        throw NoteIsDeleted ("заметка удалена")
            return false
    }

    fun restoreComment (cid: Int, noteId: Int): Boolean{
        val note = notes[noteId]
        val commentsOfNote = note!!.comments
        if (note!!.deleted== false) {
            for ((index, value) in commentsOfNote.withIndex()) {
                if (value.cid == cid) when (value.deleted) {
                    true -> {
                        commentsOfNote[index] = value.copy(deleted = false)
                        return true
                    }
                    false -> {
                        return false
                    }
                }
            }
        }
        throw NoteIsDeleted ("заметка удалена")
        return false
    }
    }





fun main (){
    val service : NotesService = NotesService ()
    service.add(0, "title", "Text", 1)
    service.add(0, "title2", "Text2", 1)
    service.add(0, "title3", "Text3", 1)
    println (service.add(0, "title", "Text", 1))
    println(service.delete(1))
    service.delete(2)
    println(service.notes[2]?.deleted ?: "pfxtv&")
    println(service.getById(3))
    println(service.getById(1))
    println(service.edit(Notes(3,"title new","Text new",1)))
    println(service.getById(13))
}

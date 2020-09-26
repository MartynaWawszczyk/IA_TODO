package pl.tysia.innovate_app.data.model

class SortingMethod {
    enum class SortingType(var value: String){
        DEADLINE("deadline_at"),
        COMPLETED_AT("completed_at"),
        INSERTED_AT("inserted_at"),
        UPDATED_AT("updated_at"),
        TITLE("title")
    }

    enum class SortingDirection(var value: String){
       ASCENDING("_asc"),
       DESCENDING("_desc")
    }
    
    var sortingType =
        SortingType.TITLE
        set(value) {
            if (value == field) changeDirection()
            else sortDirection =
                SortingDirection.ASCENDING

            field = value
        }

    var sortDirection =
        SortingDirection.ASCENDING

    private fun changeDirection(){
        sortDirection =
            if (sortDirection == SortingDirection.ASCENDING) SortingDirection.DESCENDING
            else SortingDirection.ASCENDING
    }

    override fun toString(): String {
        return sortingType.value + sortDirection.value
    }
}
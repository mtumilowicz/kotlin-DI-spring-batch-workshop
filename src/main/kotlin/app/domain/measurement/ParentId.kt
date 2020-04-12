package app.domain.measurement

data class ParentId(val raw: String) {

    override fun toString(): String {
        return "ParentId($raw)"
    }
}
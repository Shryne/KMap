package different_accessor.other_get

class User {
    private var age: Int = 0
    var name: String = ""

    fun theAge(): Int = age
    fun theAge(age: Int) {
        this.age = age
    }
}

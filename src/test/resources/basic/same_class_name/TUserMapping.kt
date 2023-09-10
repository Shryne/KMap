package basic.same_name

public fun basic.same_name.other.User.toUser(): User = User().also {
    it.age = age
    it.name = name
    it.hobbies = hobbies
}
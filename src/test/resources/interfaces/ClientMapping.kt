package interfaces

public fun Client.toUser(): User =
    User().also {
        it.age = abs(age)
        it.id = id
    }
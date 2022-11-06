package uz.minfin.project

fun Boolean.throwIfFalse(function: () -> Throwable) {
    if (!this) throw function()
}


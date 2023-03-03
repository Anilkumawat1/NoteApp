package com.anilkumawat3104.notes

class firebasemodel {
    private lateinit var title: String
    private lateinit var content: String
    constructor(title: String, content: String) {
        this.title = title
        this.content = content

    }

    constructor() {
        title = ""
        content = ""

    }

    fun getTitle(): String {
        return title
    }

    fun getContent(): String {
        return content
    }
}
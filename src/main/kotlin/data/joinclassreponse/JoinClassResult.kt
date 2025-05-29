package com.myclassroom.data.joinclassreponse

sealed class JoinClassResult {
    data object Success : JoinClassResult()
    data object ClassNotFound : JoinClassResult()
    data object StudentNotFound : JoinClassResult()
    data object AlreadyEnrolled : JoinClassResult()
}
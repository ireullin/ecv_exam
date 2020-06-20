package sessions

abstract class HitSession(val correct: Int = 0, val total: Int = 0)
class SessionBigData20200413(correct: Int = 0,  total: Int = 0):HitSession(correct, total)
class SessionAWSSecurityPart1(correct: Int = 0, total: Int = 0):HitSession(correct, total)
class SessionAWSSecurityPart2(correct: Int = 0, total: Int = 0):HitSession(correct, total)


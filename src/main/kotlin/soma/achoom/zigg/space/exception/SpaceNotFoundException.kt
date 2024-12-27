package soma.achoom.zigg.space.exception

class SpaceNotFoundException : RuntimeException() {
    override val message: String
        get() = "존재하지 않는 공간입니다."
}
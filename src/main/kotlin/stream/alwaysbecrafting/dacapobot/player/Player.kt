interface Player {
    var currentTrack: Track?

    fun nextTrack()
    fun setQueue()
    fun play()
    fun stop()
}
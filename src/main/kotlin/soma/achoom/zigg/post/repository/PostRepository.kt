package soma.achoom.zigg.post.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import soma.achoom.zigg.board.entity.Board
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.user.entity.User


interface PostRepository : JpaRepository<Post,Long>{
    override fun findAll(page: Pageable): Page<Post>

    fun findPostsByBoardAndTitleContaining(board: Board, keyword: String, page: Pageable): Page<Post>
    @Query("SELECT p FROM post p LEFT JOIN PostLike pl ON pl.post = p WHERE local_date - p.createAt <= 3 GROUP BY p HAVING COUNT(pl) >= 10 ORDER BY COUNT(pl) DESC")
    fun findBestPosts(pageable: Pageable): List<Post>
    @Query(
        value = "SELECT * FROM post WHERE board_board_id = :boardId ORDER BY RAND()",
        nativeQuery = true
    )
    fun getRandomPostsByBoardAndCount(boardId: Long, page: Pageable): List<Post>

    fun findPostsByBoard(board: Board, pageable: Pageable): Page<Post>
    fun findPostsByCreator(user: User): List<Post>
}
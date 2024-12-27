package soma.achoom.zigg.repository

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.TestConfig
import soma.achoom.zigg.board.entity.Board
import soma.achoom.zigg.board.repository.BoardRepository
import soma.achoom.zigg.comment.entity.Comment
import soma.achoom.zigg.comment.entity.CommentCreator
import soma.achoom.zigg.comment.entity.CommentType
import soma.achoom.zigg.comment.repository.CommentRepository
import soma.achoom.zigg.data.DummyDataUtil
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.entity.PostLike
import soma.achoom.zigg.post.repository.PostLikeRepository
import soma.achoom.zigg.post.repository.PostRepository
import soma.achoom.zigg.user.repository.UserRepository
import kotlin.test.Test

@SpringBootTest(classes = [TestConfig::class])
@Transactional
@ActiveProfiles("test")
class PostRepositoryTest {
    @Autowired
    private lateinit var commentRepository: CommentRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var postLikeRepository: PostLikeRepository

    @Autowired
    private lateinit var boardRepository: BoardRepository

    @Autowired
    private lateinit var dummyDataUtil: DummyDataUtil

    @Autowired
    private lateinit var postRepository: PostRepository

    @BeforeEach
    fun setUp() {
        val user = dummyDataUtil.createDummyUser()
        val board = Board(
            name = "test",
        )
        for (i in 1..10) {
            val users = dummyDataUtil.createDummyUserList(i)
            postRepository.save(
                Post(
                    creator = user,
                    title = "title$i",
                    textContent = "content$i",
                    board = board,
                    anonymous = false
                )
            )
        }
        boardRepository.save(board)
    }

    @Test
    fun `hottest post while 3 days with 0 likes posts`() {
        val posts = postRepository.findBestPosts(Pageable.ofSize(2))
        assert(posts.isEmpty())
        println(posts.size)
    }
    @Test
    fun `hottest post while 3 days with 10+a likes posts`(){
        val user = dummyDataUtil.createDummyUser()
        val board = Board(
            name = "test",
        )
        boardRepository.save(board)
        val post = Post(
            creator = user,
            title = "title",
            textContent = "content",
            board = board,
            anonymous = false
        )
        postRepository.save(post)
        for (i in 1..10) {
            val users = dummyDataUtil.createDummyUserList(i)
            postLikeRepository.saveAll(
                users.map {
                    PostLike(
                        user = it,
                        post = post
                    )
                }
            )
        }
        val posts = postRepository.findBestPosts(Pageable.ofSize(2))
        assert(posts.size == 1)
        println(posts.size)
    }

    @Test
    fun `delete post`() {
        val post = postRepository.findAll()[0]

        postRepository.delete(post)

        assert(postRepository.findById(post.postId!!).isEmpty)

    }

    @Test
    fun `post like persist test`() {
        val post = postRepository.findAll()[0]
        val user = dummyDataUtil.createDummyUser()

        val postLike = PostLike(user = user, post = post)
        postLikeRepository.save(postLike)

        assert(postLikeRepository.findAll().size == 1)
        assert(postLikeRepository.findAll()[0].post == post)
        assert(postLikeRepository.findAll()[0].user == user)

    }

    @Test
    fun `post like delete test`() {
        val post = postRepository.findAll()[0]
        val user = userRepository.save(dummyDataUtil.createDummyUser()) // user를 영속화하여 저장

        //when
        val postLike = PostLike(user = user, post = post)
        postLikeRepository.save(postLike)

        //then
        assert(postLikeRepository.findAll().size == 1)
        assert(postLikeRepository.findAll()[0].post == post)
        assert(postLikeRepository.findAll()[0].user == user)

        // when
        postLikeRepository.delete(postLike)

        // then
        assert(postLikeRepository.findAll().isEmpty())
    }

    @Test
    fun `when save post, it should save board`() {
        val user = dummyDataUtil.createDummyUser()
        val board = Board(
            name = "board",
        )
        boardRepository.save(board)

        val post = Post(
            creator = user,
            title = "title",
            textContent = "content",
            board = board,
            anonymous = false
        )
        postRepository.save(post)

    }

    @Test
    fun `when delete post it should delete comments`() {
        val user = dummyDataUtil.createDummyUser()
        val board = Board(
            name = "board",
        )
        boardRepository.save(board)
        val post = Post(
            creator = user,
            title = "title",
            textContent = "content",
            board = board,
            anonymous = false
        )
        postRepository.save(post)

        val comment1 = Comment(
            creator = CommentCreator(
                user = user,
                post = post,
                anonymous = false
            ),
            textComment = "comment",
            post = post,
            commentType = CommentType.COMMENT
        )
        val comment2 = Comment(
            creator = CommentCreator(
                user = user,
                post = post,
                anonymous = false
            ),
            textComment = "comment",
            post = post,
            commentType = CommentType.REPLY
        )
        comment1.replies.add(comment2)
        postRepository.save(post)
        postRepository.delete(post)
        println(commentRepository.findCommentsByCreatorUser(user).size)
        assert(commentRepository.findCommentsByCreatorUser(user).isEmpty())
    }

    @Test
    fun `get random post data`(){
        val posts = postRepository.getRandomPostsByBoardAndCount(1L,PageRequest.of(0,2))
        println(posts.size)
    }

}
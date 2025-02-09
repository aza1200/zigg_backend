package soma.achoom.zigg.history.entity

import jakarta.persistence.*
import org.springframework.context.annotation.Primary
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.content.entity.Video
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.feedback.entity.Feedback
import soma.achoom.zigg.space.entity.Space
import java.util.UUID

@Entity(name = "history")
class History(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var historyId: Long? = null,
    @Column(name = "history_name")
    var name: String?,
    @OneToOne(cascade = [CascadeType.PERSIST,CascadeType.MERGE])
    var videoKey: Video,
    @OneToOne(cascade = [CascadeType.PERSIST,CascadeType.MERGE])
    var videoThumbnailUrl: Image,
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var feedbacks: MutableSet<Feedback> = mutableSetOf(),

    ) : BaseEntity() {
}
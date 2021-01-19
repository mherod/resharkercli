package resharker.jiracli

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JiraRest2Issue(
    @SerialName("expand")
    val expand: String,
    @SerialName("fields")
    override val fields: Fields,
    @SerialName("id")
    override val id: String,
    @SerialName("key")
    override val key: String,
    @SerialName("self")
    override val self: String,
) : JiraRestIssue {

    @Serializable
    data class Fields(
        @SerialName("aggregateprogress")
        val aggregateProgress: AggregateProgress?,
        @SerialName("aggregatetimeestimate")
        val aggregatetimeestimate: Int? = null,
        @SerialName("aggregatetimeoriginalestimate")
        val aggregatetimeoriginalestimate: Int? = null,
        @SerialName("aggregatetimespent")
        val aggregatetimespent: String? = null,
        @SerialName("assignee")
        val assignee: Assignee? = null,
        @SerialName("attachment")
        val attachment: List<Attachments> = emptyList(),
        @SerialName("comment")
        val comment: Comments = Comments(),
        @SerialName("components")
        val components: List<String> = emptyList(),
        @SerialName("created")
        val created: String,
        @SerialName("creator")
        val creator: Creator,
        @SerialName("description")
        val description: String = "",
        @SerialName("duedate")
        val duedate: String? = null,
        @SerialName("environment")
        val environment: String? = null,
        @SerialName("fixVersions")
        val fixVersions: List<String> = emptyList(),
        @SerialName("issuelinks")
        val issueLinks: List<IssueLink> = emptyList(),
        @SerialName("issuerestriction")
        val issueRestriction: IssueRestriction? = null,
        @SerialName("issuetype")
        val issueType: IssueType,
        @SerialName("labels")
        val labels: List<String> = emptyList(),
        @SerialName("lastViewed")
        val lastViewed: String? = null,
        @SerialName("priority")
        val priority: Priority,
        @SerialName("progress")
        val progress: Progress,
        @SerialName("project")
        val project: Project,
        @SerialName("reporter")
        val reporter: Reporter,
        @SerialName("resolution")
        val resolution: Resolution?,
        @SerialName("resolutiondate")
        val resolutionDate: String? = null,
        @SerialName("security")
        val security: String? = null,
        @SerialName("status")
        override val status: Status,
        @SerialName("statuscategorychangedate")
        val statuscategorychangedate: String,
        @SerialName("subtasks")
        val subtasks: List<String> = emptyList(),
        @SerialName("summary")
        override val summary: String = "",
        @SerialName("timeestimate")
        val timeestimate: Int? = null,
        @SerialName("timeoriginalestimate")
        val timeoriginalestimate: Int? = null,
        @SerialName("timespent")
        val timespent: String? = null,
        @SerialName("timetracking")
        val timeTracking: TimeTracking? = null,
        @SerialName("updated")
        val updated: String,
        @SerialName("versions")
        val versions: List<String> = emptyList(),
        @SerialName("votes")
        val votes: Votes,
        @SerialName("watches")
        val watches: Watches,
        @SerialName("worklog")
        val worklog: Worklog?,
        @SerialName("workratio")
        val workratio: Int? = null,
    ) : JiraRestIssueFields {

        @Serializable
        data class AggregateProgress(
            @SerialName("percent")
            val percent: Int? = null,
            @SerialName("progress")
            val progress: Int? = null,
            @SerialName("total")
            val total: Int? = null,
        )

        @Serializable
        data class Assignee(
            @SerialName("accountId")
            val accountId: String,
            @SerialName("accountType")
            val accountType: String,
            @SerialName("active")
            val active: Boolean,
            @SerialName("avatarUrls")
            val avatarUrls: AvatarUrls,
            @SerialName("displayName")
            val displayName: String,
            @SerialName("self")
            val self: String,
            @SerialName("timeZone")
            val timeZone: String,
        ) {
            @Serializable
            data class AvatarUrls(
                @SerialName("16x16")
                val x16: String,
                @SerialName("24x24")
                val x24: String,
                @SerialName("32x32")
                val x32: String,
                @SerialName("48x48")
                val x48: String,
            )
        }

        @Serializable
        data class Comments(
            @SerialName("comments")
            val comments: List<Comment> = emptyList(),
            @SerialName("maxResults")
            val maxResults: Int = 0,
            @SerialName("startAt")
            val startAt: Int = 0,
            @SerialName("total")
            val total: Int = 0,
        )

        @Serializable
        data class Comment(
            @SerialName("self")
            val self: String,
        )

        @Serializable
        data class Attachments(
            @SerialName("self")
            val self: String,
        )

        @Serializable
        data class Creator(
            @SerialName("accountId")
            val accountId: String,
            @SerialName("accountType")
            val accountType: String,
            @SerialName("active")
            val active: Boolean,
            @SerialName("avatarUrls")
            val avatarUrls: AvatarUrls,
            @SerialName("displayName")
            val displayName: String,
            @SerialName("self")
            val self: String,
            @SerialName("timeZone")
            val timeZone: String,
        ) {
            @Serializable
            data class AvatarUrls(
                @SerialName("16x16")
                val x16: String,
                @SerialName("24x24")
                val x24: String,
                @SerialName("32x32")
                val x32: String,
                @SerialName("48x48")
                val x48: String,
            )
        }

        @Serializable
        data class IssueLink(
            @SerialName("id")
            val id: String,
            @SerialName("inwardIssue")
            val inwardIssue: InwardIssue? = null,
            @SerialName("self")
            val self: String,
            @SerialName("type")
            val type: Type,
        ) {
            @Serializable
            data class InwardIssue(
                @SerialName("fields")
                val fields: Fields,
                @SerialName("id")
                val id: String,
                @SerialName("key")
                val key: String,
                @SerialName("self")
                val self: String,
            ) {
                @Serializable
                data class Fields(
                    @SerialName("issuetype")
                    val issueType: IssueType,
                    @SerialName("priority")
                    val priority: Priority,
                    @SerialName("status")
                    val status: Status,
                    @SerialName("summary")
                    val summary: String,
                ) {
                    @Serializable
                    data class IssueType(
                        @SerialName("avatarId")
                        val avatarId: Int? = null,
                        @SerialName("description")
                        val description: String,
                        @SerialName("iconUrl")
                        val iconUrl: String,
                        @SerialName("id")
                        val id: String,
                        @SerialName("name")
                        val name: String,
                        @SerialName("self")
                        val self: String,
                        @SerialName("subtask")
                        val subtask: Boolean,
                    )

                    @Serializable
                    data class Priority(
                        @SerialName("iconUrl")
                        val iconUrl: String,
                        @SerialName("id")
                        val id: String,
                        @SerialName("name")
                        val name: String,
                        @SerialName("self")
                        val self: String,
                    )

                    @Serializable
                    data class Status(
                        @SerialName("description")
                        val description: String,
                        @SerialName("iconUrl")
                        val iconUrl: String,
                        @SerialName("id")
                        val id: String,
                        @SerialName("name")
                        val name: String,
                        @SerialName("self")
                        val self: String,
                        @SerialName("statusCategory")
                        val statusCategory: StatusCategory,
                    ) {
                        @Serializable
                        data class StatusCategory(
                            @SerialName("colorName")
                            val colorName: String,
                            @SerialName("id")
                            val id: Int? = null,
                            @SerialName("key")
                            val key: String,
                            @SerialName("name")
                            val name: String,
                            @SerialName("self")
                            val self: String,
                        )
                    }
                }
            }

            @Serializable
            data class Type(
                @SerialName("id")
                val id: String,
                @SerialName("inward")
                val inward: String? = null,
                @SerialName("name")
                val name: String,
                @SerialName("outward")
                val outward: String? = null,
                @SerialName("self")
                val self: String,
            )
        }

        @Serializable
        data class IssueRestriction(
            @SerialName("issuerestrictions")
            val issueRestrictions: IssueRestrictions,
            @SerialName("shouldDisplay")
            val shouldDisplay: Boolean,
        ) {
            @Serializable
            class IssueRestrictions
        }

        @Serializable
        data class IssueType(
            @SerialName("avatarId")
            val avatarId: Int? = null,
            @SerialName("description")
            val description: String,
            @SerialName("iconUrl")
            val iconUrl: String,
            @SerialName("id")
            val id: String,
            @SerialName("name")
            val name: String,
            @SerialName("self")
            val self: String,
            @SerialName("subtask")
            val subtask: Boolean,
        )

        @Serializable
        data class Priority(
            @SerialName("iconUrl")
            val iconUrl: String,
            @SerialName("id")
            val id: String,
            @SerialName("name")
            val name: String,
            @SerialName("self")
            val self: String,
        )

        @Serializable
        data class Progress(
            @SerialName("percent")
            val percent: Int? = null,
            @SerialName("progress")
            val progress: Int? = null,
            @SerialName("total")
            val total: Int? = null,
        )

        @Serializable
        data class Project(
            @SerialName("avatarUrls")
            val avatarUrls: AvatarUrls,
            @SerialName("id")
            val id: String,
            @SerialName("key")
            val key: String,
            @SerialName("name")
            val name: String,
            @SerialName("projectTypeKey")
            val projectTypeKey: String,
            @SerialName("self")
            val self: String,
            @SerialName("simplified")
            val simplified: Boolean,
        ) {
            @Serializable
            data class AvatarUrls(
                @SerialName("16x16")
                val x16: String,
                @SerialName("24x24")
                val x24: String,
                @SerialName("32x32")
                val x32: String,
                @SerialName("48x48")
                val x48: String,
            )
        }

        @Serializable
        data class Reporter(
            @SerialName("accountId")
            val accountId: String,
            @SerialName("accountType")
            val accountType: String,
            @SerialName("active")
            val active: Boolean,
            @SerialName("avatarUrls")
            val avatarUrls: AvatarUrls,
            @SerialName("displayName")
            val displayName: String,
            @SerialName("self")
            val self: String,
            @SerialName("timeZone")
            val timeZone: String,
        ) {
            @Serializable
            data class AvatarUrls(
                @SerialName("16x16")
                val x16: String,
                @SerialName("24x24")
                val x24: String,
                @SerialName("32x32")
                val x32: String,
                @SerialName("48x48")
                val x48: String,
            )
        }

        @Serializable
        data class Resolution(
            @SerialName("description")
            val description: String,
            @SerialName("id")
            val id: String,
            @SerialName("name")
            val name: String,
            @SerialName("self")
            val self: String,
        )

        @Serializable
        data class Status(
            @SerialName("description")
            val description: String,
            @SerialName("iconUrl")
            val iconUrl: String,
            @SerialName("id")
            val id: String,
            @SerialName("name")
            override val name: String,
            @SerialName("self")
            val self: String,
            @SerialName("statusCategory")
            val statusCategory: StatusCategory,
        ) : JiraRestIssueStatus {

            @Serializable
            data class StatusCategory(
                @SerialName("colorName")
                val colorName: String,
                @SerialName("id")
                val id: Int? = null,
                @SerialName("key")
                val key: String,
                @SerialName("name")
                val name: String,
                @SerialName("self")
                val self: String,
            )
        }

        @Serializable
        data class TimeTracking(
            @SerialName("originalEstimate")
            val originalEstimate: String? = null,
            @SerialName("originalEstimateSeconds")
            val originalEstimateSeconds: Int? = null,
            @SerialName("remainingEstimate")
            val remainingEstimate: String? = null,
            @SerialName("remainingEstimateSeconds")
            val remainingEstimateSeconds: Int? = null,
        )

        @Serializable
        data class Votes(
            @SerialName("hasVoted")
            val hasVoted: Boolean,
            @SerialName("self")
            val self: String,
            @SerialName("votes")
            val votes: Int? = null,
        )

        @Serializable
        data class Watches(
            @SerialName("isWatching")
            val isWatching: Boolean,
            @SerialName("self")
            val self: String,
            @SerialName("watchCount")
            val watchCount: Int? = null,
        )

        @Serializable
        data class Worklog(
            @SerialName("maxResults")
            val maxResults: Int = 0,
            @SerialName("startAt")
            val startAt: Int = 0,
            @SerialName("total")
            val total: Int = 0,
            @SerialName("worklogs")
            val worklogs: List<WorklogItem> = emptyList(),
        )

        @Serializable
        data class WorklogItem(
            @SerialName("self")
            val self: String,
        )
    }
}

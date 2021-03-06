package io.petros.reviews.presentation.feature.reviews.list

import android.support.v7.widget.RecyclerView
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import io.petros.reviews.presentation.PreconfiguredRobolectricTestRunner
import io.petros.reviews.presentation.RobolectricTestProvider.Companion.provideContext
import io.petros.reviews.presentation.feature.common.list.adapter.AdapterStatus
import io.petros.reviews.presentation.feature.common.list.holder.ErrorViewHolder
import io.petros.reviews.presentation.feature.common.list.holder.ProgressViewHolder
import io.petros.reviews.presentation.feature.reviews.list.ReviewsAdapter.Companion.VIEW_TYPE_ERROR
import io.petros.reviews.presentation.feature.reviews.list.ReviewsAdapter.Companion.VIEW_TYPE_PROGRESS
import io.petros.reviews.presentation.feature.reviews.list.ReviewsAdapter.Companion.VIEW_TYPE_REVIEW
import io.petros.reviews.test.domain.TestReviewsProvider.Companion.provideReview
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(PreconfiguredRobolectricTestRunner::class)
class ReviewsAdapterRobolectricTest {

    private val recyclerView = RecyclerView(provideContext())

    private val items = listOf(provideReview(id = 1), provideReview(id = 2), provideReview(id = 3))

    private lateinit var testedClass: ReviewsAdapter

    @Before
    fun setUp() {
        testedClass = ReviewsAdapter()
        testedClass.items.addAll(items)
    }

    /* VIEW HOLDER */

    @Test
    fun `When creating a view holder for a progress item, then the correct view holder is returned`() {
        testedClass.onAttachedToRecyclerView(recyclerView)

        val viewHolder = testedClass.onCreateViewHolder(mock(), VIEW_TYPE_PROGRESS)

        assertThat(viewHolder).isInstanceOf(ProgressViewHolder::class.java)
    }

    @Test
    fun `When creating a view holder for a review item, then the correct view holder is returned`() {
        testedClass.onAttachedToRecyclerView(recyclerView)

        val viewHolder = testedClass.onCreateViewHolder(mock(), VIEW_TYPE_REVIEW)

        assertThat(viewHolder).isInstanceOf(ReviewViewHolder::class.java)
    }

    @Test
    fun `When creating a view holder for an error item, then the correct view holder is returned`() {
        testedClass.onAttachedToRecyclerView(recyclerView)

        val viewHolder = testedClass.onCreateViewHolder(mock(), VIEW_TYPE_ERROR)

        assertThat(viewHolder).isInstanceOf(ErrorViewHolder::class.java)
    }

    @Test
    fun `Given a review view type, when binding a view holder, then a review item is bind`() {
        testedClass.onAttachedToRecyclerView(recyclerView)
        testedClass.onCreateViewHolder(mock(), VIEW_TYPE_REVIEW)
        val position = 1
        val viewHolderMock = mock<ReviewViewHolder>()

        testedClass.onBindViewHolder(viewHolderMock, position)

        verify(viewHolderMock).bind(items[position])
    }

    @Test
    fun `Given a non-review view type, when binding a view holder, then a review item is not bind`() {
        testedClass.onAttachedToRecyclerView(recyclerView)
        testedClass.onCreateViewHolder(mock(), VIEW_TYPE_PROGRESS)
        testedClass.status = AdapterStatus.LOADING
        val position = items.size
        val viewHolderMock = mock<ReviewViewHolder>()

        testedClass.onBindViewHolder(viewHolderMock, position)

        verifyZeroInteractions(viewHolderMock)
    }

    /* NAVIGATION */

    @Test
    fun `When getting the item view type of a review item, then a review view type is returned`() {
        val result = testedClass.getItemViewType(1)

        assertThat(result).isEqualTo(VIEW_TYPE_REVIEW)
    }

    @Test
    fun `Given at last position and idle, when getting the item view type, then a review item view type is returned`() {
        testedClass.status = AdapterStatus.IDLE

        val result = testedClass.getItemViewType(items.size)

        assertThat(result).isEqualTo(VIEW_TYPE_REVIEW)
    }

    @Test
    fun `Given at last position and loading, when getting the item view type, then a progress view type is returned`() {
        testedClass.status = AdapterStatus.LOADING

        val result = testedClass.getItemViewType(items.size)

        assertThat(result).isEqualTo(VIEW_TYPE_PROGRESS)
    }

    @Test
    fun `Given at last position and error, when getting the item view type, then an error view type is returned`() {
        testedClass.status = AdapterStatus.ERROR

        val result = testedClass.getItemViewType(items.size)

        assertThat(result).isEqualTo(VIEW_TYPE_ERROR)
    }

}

package com.testandroid.pagingtestapp.paging

import com.testandroid.pagingtestapp.data.NewContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 0

/**
 * Repository class that works with local and remote data sources.
 */
class ContentRepository(private val service: ContentService) {

    // keep the list of all results received
    private val inMemoryCache = mutableListOf<NewContent>()

    // shared flow of results, which allows us to broadcast updates so
    // the subscriber will have the latest data
    private val searchResults = MutableSharedFlow<RepoSearchResult>(replay = 1)

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = STARTING_PAGE_INDEX

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    /**
     * Search repositories whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    suspend fun getSearchResultStream(
        query: String,
        repositoryUrl: String
    ): Flow<RepoSearchResult> {
        lastRequestedPage = 1
        inMemoryCache.clear()
        requestAndSaveData(query, repositoryUrl)
        lastRequestedPage++
        return searchResults
    }

    suspend fun requestMore(query: String, repositoryUrl: String) {
        if (isRequestInProgress) return
        val successful = requestAndSaveData(query, repositoryUrl)
        if (successful) {
            lastRequestedPage++
        }
    }

    private suspend fun requestAndSaveData(query: String, repositoryUrl: String): Boolean {
        isRequestInProgress = true
        var successful = false

        try {
            val response = service.searchRepos(
                repositoryUrl,
                lastRequestedPage,
                NETWORK_PAGE_SIZE
            )

            val repos = response.items
            inMemoryCache.addAll(repos)
            val reposByName = reposByName(query)
            searchResults.emit(RepoSearchResult.Success(reposByName))
            successful = true
        } catch (exception: IOException) {
            searchResults.emit(RepoSearchResult.Error(exception))
        } catch (exception: HttpException) {
            searchResults.emit(RepoSearchResult.Error(exception))
        }
        isRequestInProgress = false
        return successful
    }

    private fun reposByName(query: String): List<NewContent> {
        // from the in memory cache select only the repos whose name or description matches
        // the query. Then order the results.
        return inMemoryCache.sortedWith(compareByDescending { it.createdAt })
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }
}

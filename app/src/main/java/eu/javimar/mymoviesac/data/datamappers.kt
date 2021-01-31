package eu.javimar.mymoviesac.data

import eu.javimar.domain.Movie
import eu.javimar.mymoviesac.data.database.Movie as DomainMovie
import eu.javimar.mymoviesac.data.server.Movie as ServerMovie


fun Movie.toRoomMovie(): DomainMovie =
    DomainMovie(
        id,
        title,
        overview,
        releaseDate,
        posterPath,
        backdropPath,
        originalLanguage,
        originalTitle,
        popularity,
        voteAverage,
        favorite
    )

fun DomainMovie.toDomainMovie(): Movie =
    Movie(
    id,
    title,
    overview,
    releaseDate,
    posterPath,
    backdropPath,
    originalLanguage,
    originalTitle,
    popularity,
    voteAverage,
    favorite
)

fun ServerMovie.toDomainMovie(): Movie =
    Movie(
        id,
        title,
        overview,
        releaseDate,
        "https://image.tmdb.org/t/p/w185/$posterPath",
        "https://image.tmdb.org/t/p/w780${backdropPath ?: posterPath}",
        originalLanguage,
        originalTitle,
        popularity,
        voteAverage,
        false
    )
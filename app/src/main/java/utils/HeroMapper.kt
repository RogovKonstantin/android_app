package utils

import utils.db.HeroEntity

object HeroMapper {
    fun toEntity(hero: HeroModel): HeroEntity {
        return HeroEntity(
            id = hero.id,
            firstName = hero.firstName,
            lastName = hero.lastName,
            fullName = hero.fullName,
            title = hero.title,
            family = hero.family,
            image = hero.image,
            imageUrl = hero.imageUrl
        )
    }

    fun toModel(entity: HeroEntity): HeroModel {
        return HeroModel(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            fullName = entity.fullName,
            title = entity.title,
            family = entity.family,
            image = entity.image,
            imageUrl = entity.imageUrl,
            isPlaceholder = false
        )
    }
}

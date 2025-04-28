package com.example.blank.service.entity.services

import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import com.example.blank.dto.ContentDto
import com.example.blank.entity.updateTimestamp
import com.example.blank.dto.toEntity
import com.example.blank.repository.ContentRepository
import com.example.blank.entity.ContentEntity

@Service
class ContentService(
    private val contentRepository: ContentRepository
) {

    fun addContent(content: ContentDto) {
        contentRepository.save(content.toEntity())
    }

//    fun getContentByContentId(contentId: Long): ContentEntity {
//        return contentRepository.findByContentId(contentId) ?: throw ContentNotFoundException("Content with contentId $contentId not found")
//    }

    fun getContentByContentId(contentId: Long): ContentEntity {
        return contentRepository.findById(contentId)
            .orElseThrow { ContentNotFoundException("Content with contentId $contentId not found") }
    }

//    fun getContentByTopicId(topicId: Long): ContentEntity {
//        return contentRepository.findAllByTopicId(topicId) ?: throw ContentNotFoundException("Content with topicId $topicId not found")
//    }

    fun getAllContentByTopicId(topicId: Long): List<ContentEntity> {
        return contentRepository.findAllByTopicId(topicId) ?: throw ContentNotFoundException("Content with topicId $topicId not found")
    }

    fun getAllContentByType(type: String): List<ContentEntity> {
        return contentRepository.findAllByType(type) ?: throw ContentNotFoundException("No content with type $type found")
    }

//    @Transactional
//    fun deleteContentByContentId(contentId: Long): ContentEntity {
//        val content = contentRepository.findByContentId(contentId) ?: throw ContentNotFoundException("Content with contentId $contentId not found")
//        contentRepository.deleteByContentId(contentId)
//        return content
//    }

    @Transactional
    fun deleteContentByContentId(contentId: Long): Boolean {
        return if (contentRepository.existsById(contentId)) {
            contentRepository.deleteById(contentId)
            true
        } else {
            false
        }
    }

//    @Transactional
//    fun deleteContentByTopicId(topicId: Long): ContentEntity {
//        val content = contentRepository.findAllByTopicId(topicId) ?: throw ContentNotFoundException("Content with topicId $topicId not found")
//        contentRepository.deleteAllByTopicId(topicId)
//        return content
//    }

    @Transactional
    fun deleteAllContentByTopicId(topicId: Long): Boolean {
        return if (contentRepository.existsByTopicId(topicId)) {
            contentRepository.deleteAllByTopicId(topicId)
            true
        } else {
            false
        }
    }

//    @Transactional
//    fun deleteAllContentByType(type: String): List<ContentEntity> {
//        val contentList = contentRepository.deleteAllByType(type) ?: throw ContentNotFoundException("No content with type $type found")
//        return contentList
//    }

    @Transactional
    fun deleteAllContentByType(type: String): Boolean {
        return if (contentRepository.existsByType(type)) {
            contentRepository.deleteAllByType(type)
            true
        } else {
            false
        }
    }

//    @Transactional
//    fun updateContentType(contentId: Long, newType: String): ContentEntity {
//        val content = contentRepository.findByContentId(contentId) ?: throw ContentNotFoundException("Content with contentId $contentId not found")
//        content.type = newType
//        content.updateTimestamp()
//        return contentRepository.save(content)
//    }

    @Transactional
    fun updateContentType(contentId: Long, newType: String): ContentEntity {
        val content = contentRepository.findById(contentId)
            .orElseThrow { ContentNotFoundException("Content with contentId $contentId not found") }
        content.type = newType
        content.updateTimestamp()
        return contentRepository.save(content)
    }

//    @Transactional
//    fun updateContentData(contentId: Long, newData: String): ContentEntity {
//        val content = contentRepository.findByContentId(contentId) ?: throw ContentNotFoundException("Content with contentId $contentId not found")
//        content.contentData = newData
//        content.updateTimestamp()
//        return contentRepository.save(content)
//    }

    @Transactional
    fun updateContentData(contentId: Long, newData: String): ContentEntity {
        val content = contentRepository.findById(contentId)
            .orElseThrow { ContentNotFoundException("Content with contentId $contentId not found") }
        content.contentData = newData
        content.updateTimestamp()
        return contentRepository.save(content)
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class ContentNotFoundException(message: String) : RuntimeException(message)
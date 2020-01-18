package it.unical.demacs.asde.signme.repositories;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.unical.demacs.asde.signme.model.Notice;

@Repository
public interface NoticeDAO extends CrudRepository<Notice, Integer> {

	Set<Notice> findNoticesByCourseCourseId(Integer courseId);

}

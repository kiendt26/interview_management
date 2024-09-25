package fa.training.services;

import fa.training.dto.JobDTO;
import fa.training.entities.Job;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JobService {
    Job convertDTO2Entity(JobDTO jobDTO, Job job);
    JobDTO convertEntity2DTO(Job job, JobDTO jobDTO);
    Page<JobDTO> getAll(int page,int size);
    JobDTO save(JobDTO jobDTO);
    JobDTO update(JobDTO jobDTO);
    JobDTO delete(Long id);
    JobDTO findById(Long id);
     Page<JobDTO> searchJob(String keyword, String status, int page, int size);

    }

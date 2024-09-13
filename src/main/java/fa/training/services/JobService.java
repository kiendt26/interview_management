package fa.training.services;

import fa.training.dto.JobDTO;
import fa.training.entities.Job;

import java.util.List;

public interface JobService {
    Job convertDTO2Entity(JobDTO jobDTO, Job job);
    JobDTO convertEntity2DTO(Job job, JobDTO jobDTO);
    List<JobDTO> getAll();
    JobDTO save(JobDTO jobDTO);
    JobDTO update(JobDTO jobDTO);
    JobDTO delete(Long id);
    JobDTO findById(Long id);
}

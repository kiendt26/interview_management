package fa.training.services.impl;

import fa.training.dto.JobDTO;
import fa.training.entities.Job;
import fa.training.repositories.JobRepository;
import fa.training.services.JobService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class JobDTOImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public Job convertDTO2Entity(JobDTO jobDTO, Job job) {
         if(job == null){
             job = new Job();
         }

        BeanUtils.copyProperties(jobDTO,job,"skillsDTO","benefitDTO","levelDTO");
       String[] skillsList = jobDTO.getSkillsDTO();
        String[] benefitList = jobDTO.getBenefitDTO();
        String[] levelList = jobDTO.getLevelDTO();

        String temp1 = "";
        if(skillsList != null){
            for (String skill : skillsList) {
                temp1 += skill + ",";

            }
            job.setSkills(temp1);
        }

        String temp2 = "";
        if(benefitList != null){
            for (String benefit : benefitList) {
                temp2 += benefit + ",";

            }
            job.setBenefit(temp2);
        }

        String temp3 = "";
        if(levelList != null){
            for (String level : levelList) {
                temp3 += level + ",";

            }
            job.setLevel(temp3);
        }
        return job;

    }

    @Override
    public JobDTO convertEntity2DTO(Job job, JobDTO jobDTO) {
        if(jobDTO == null){
            jobDTO = new JobDTO();
        }
        BeanUtils.copyProperties(job,jobDTO);
        String skills = job.getSkills();
        if(skills != null) {


            String[] skillDTO = skills.split(",");
            jobDTO.setSkillsDTO(skillDTO);
        }

        String benefit = job.getBenefit();
        if(benefit != null) {


            String[] benefitDTO = benefit.split(",");
            jobDTO.setBenefitDTO(benefitDTO);
        }

        String level = job.getLevel();
        if(level != null) {


            String[] levelDTO = level.split(",");
            jobDTO.setLevelDTO(levelDTO);
        }
        return jobDTO;

    }

    @Override
    public Page<JobDTO> getAll(int page,int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<JobDTO> jobDTOList = new ArrayList<>();
        Page<Job> jobList = jobRepository.findAll(pageable);
        LocalDate currentDate = LocalDate.now();


        for (Job job : jobList) {
            job.setStatus("Draft");
            if (job.getEndDate().isBefore(currentDate)) {
                job.setStatus("Closed");
            }
            if (job.getStartDate().isAfter(currentDate)) {
                job.setStatus("Open");

            }


            // Lưu lại trạng thái mới nếu có thay đổi
            jobRepository.save(job);
        }

        for (Job job : jobList) {
            JobDTO dto = convertEntity2DTO(job,null);
            jobDTOList.add(dto);

        }
        return jobList.map(job -> {
            JobDTO dto = convertEntity2DTO(job,null);
            return dto;
        });
    }

    @Override
    public JobDTO save(JobDTO jobDTO) {
        Job job = convertDTO2Entity(jobDTO,null);
        jobRepository.save(job);
        jobDTO.setJobId(job.getJobId());
        return jobDTO;
    }

    @Override
    public JobDTO update(JobDTO jobDTO) {
        return null;
    }

    @Override
    public JobDTO delete(Long id) {
        Job job =  jobRepository.findById(id).orElse(null);
        if(job != null){
            jobRepository.delete(job);
        }

        return convertEntity2DTO(job,null);    }

    @Override
    public JobDTO findById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        return convertEntity2DTO(job,null);    }

    @Override
    public Page<JobDTO> searchJob(String keyword, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if ((keyword == null || keyword.isEmpty()) && (status == null || status.isEmpty())) {
            return getAll(page,size);

        } else if (status == null || status.isEmpty()) {
            return jobRepository.findByKeyword(keyword,pageable);
        } else if (keyword == null || keyword.isEmpty()) {
            return jobRepository.findByStatus(status, pageable);
        } else {
            return jobRepository.findByKeywordAndStatus(keyword, status, pageable);
        }
    }


}


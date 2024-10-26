package org.cosmic.backend.domain.File.applications;

import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.File.exceptions.ImageSaveFailedException;
import org.cosmic.backend.domain.File.exceptions.NotFoundFileException;
import org.cosmic.backend.domain.File.exceptions.NotReadableException;
import org.cosmic.backend.domain.File.utils.FileNameUtil;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    @Value("${file.upload-dir}")
    private String uploadDirectory;

    @Autowired
    private UsersRepository usersRepository;

    @Transactional
    public void uploadFile(Long userId, MultipartFile profileImage) {
        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String originalFileName = profileImage.getOriginalFilename();
        String saveImgFileName = FileNameUtil.fileNameConvert(originalFileName).replaceAll("\\.\\w+$", ".webp");
        String fullPath = uploadDirectory +"/"+ saveImgFileName;
        User user = usersRepository.findById(userId)
                .orElseThrow(NotFoundUserException::new);
        // 프로필 이미지 파일 저장
        if(!profileImage.isEmpty()) {
            try {
                profileImage.transferTo(new File(fullPath));
            } catch (IOException e) {
                throw new ImageSaveFailedException();
            }
        }

        user.setProfilePicture(saveImgFileName);
        usersRepository.save(user);
    }


    @Transactional
    public Resource getImageFile(String fileName) throws MalformedURLException {
        Path filePath = Paths.get(uploadDirectory).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if(!resource.exists()) {
            throw new NotFoundFileException();
        }
        if(!resource.isReadable()) {
            throw new NotReadableException();
        }
        return resource;
    }
}
package ru.hogwarts.school.model;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "avatar")
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String filePath; //путь к файлу на диске
    private long fileSize; //размер файла
    private String mediaType; //тип файла

//    @Lob вроде может присвоить oid  весь день копался, ошибка оказалась в обычно сеттере, но эту строку все равно не раскомичу, и без нее все работает
    @Column(name = "preview", columnDefinition = "bytea") //явно задает имя и тип таблицы
    private byte[] preview;

    @OneToOne
    @JoinColumn(name = "student_id") //явно указывает имя связующей колонки в таблице, хоть автоматика и поставит точно такое же
    private Student student;

    public Avatar() { //Без пустого конструктора hibernate не сможет создать объект
    }

    public Avatar(String filePath, long fileSize, String mediaType, byte[] preview) {
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.preview = preview;
    }

    public Avatar(Long id, String filePath, long fileSize, String mediaType, byte[] preview, Student student) {
        this.id = id;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.preview = preview;
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public byte[] getPreview() {
        return preview;
    }

    public Student getStudent() {
        return student;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setPreview(byte[] preview) {
        this.preview = preview;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Avatar avatar = (Avatar) o;
        return Objects.equals(id, avatar.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", mediaType='" + mediaType + '\'' +
                ", preview=" + Arrays.toString(preview) +
                ", student=" + student +
                '}';
    }
}

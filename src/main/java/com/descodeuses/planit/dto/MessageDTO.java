package com.descodeuses.planit.dto;

import java.time.LocalDateTime;

public class MessageDTO {

    private Long id;
    private Long todoId;
    private Long authorId;
    private String authorSurname;
    private String authorName;
    private String content;
    private LocalDateTime createdAt;

    public MessageDTO() {}

    public MessageDTO(Long id, Long todoId, Long authorId, String surname, String name, String content, LocalDateTime createdAt) {
        this.id = id;
        this.todoId = todoId;
        this.authorId = authorId;
        this.authorSurname = surname;
        this.authorName = name;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() 
    { return id; 

    }

    public Long getTodoId() 
    { return todoId; 

    }

    public Long getAuthorId() 
    { return authorId; 

    }

    public String getAuthorSurname() 
    { return authorSurname; 

    }

    public String getAuthorName() 
    { return authorName; 

    }

    public String getContent() 
    { return content; 

    }

    public LocalDateTime getCreatedAt() 
    { return createdAt; 

    }

    public void setId(Long id) 
    { this.id = id; 

    }

    public void setTodoId(Long todoId) 
    { this.todoId = todoId; 

    }

    public void setAuthorId(Long authorId) 
    { this.authorId = authorId; 

    }

    public void setAuthorSurname(String authorSurname) 
    { this.authorSurname = authorSurname; 

    }

    public void setAuthorName(String authorName) 
    { this.authorName = authorName; 

    }

    public void setContent(String content) 
    { this.content = content; 

    }

    public void setCreatedAt(LocalDateTime createdAt) 
    { this.createdAt = createdAt; 
        
    }
}

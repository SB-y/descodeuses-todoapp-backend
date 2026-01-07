package com.descodeuses.planit.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "todo_message")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Message rattaché à une tâche (todo)
    @ManyToOne
    @JoinColumn(name = "todo_id", nullable = false)
    private ActionEntity todo;

    // Auteur du message
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private UtilisateurEntity author;

    @Column(nullable = false)
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() 
    { return id; 

    }

    public ActionEntity getTodo() 
    { return todo; 

    }

    public void setTodo(ActionEntity todo) 
    { this.todo = todo; 

    }

    public UtilisateurEntity getAuthor() 
    { return author; 

    }

    public void setAuthor(UtilisateurEntity author) 
    { this.author = author; 

    }

    public String getContent() 
    { return content; 

    }

    public void setContent(String content) 
    { this.content = content; 

    }

    public LocalDateTime getCreatedAt() 
    { return createdAt; 
        
    }
}

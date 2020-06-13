package com.github.noteitdown.note.processor.event;

import com.github.noteitdown.note.domain.Note;
import lombok.Value;

/**
 * Created by cenkakin
 */
@Value
public class NoteProcessedEvent {

    Note note;
}

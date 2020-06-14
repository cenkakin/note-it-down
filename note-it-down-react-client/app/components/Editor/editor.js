import {
  convertToRaw,
  convertFromRaw,
  Editor,
  EditorState,
  RichUtils,
} from 'draft-js';
import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import StyledPaper from '../../containers/WorkspacePage/StyledPaper';

export default function CustomEditor({ content, onEditorChange }) {
  const [editorState, setEditorState] = React.useState(
    EditorState.createEmpty(),
  );
  const editor = React.useRef(null);
  const onChange = st => {
    setEditorState(st);
  };

  function focusEditor() {
    editor.current.focus();
  }

  const handleKeyCommand = (command, st) => {
    const newState = RichUtils.handleKeyCommand(st, command);
    if (newState) {
      onChange(newState);
      return 'handled';
    }
    return 'not-handled';
  };

  useEffect(() => {
    focusEditor();
  }, []);

  useEffect(() => {
    const timer = setTimeout(() => {
      onEditorChange(convertToRaw(editorState.getCurrentContent()), null, 2);
    }, 2000);

    return () => clearTimeout(timer);
  }, [editorState]);

  useEffect(() => {
    if (content) {
      const st = EditorState.createWithContent(convertFromRaw(content));
      onChange(st);
    }
  }, [content]);

  return (
    <div>
      <StyledPaper onClick={focusEditor} elevator={15}>
        <Editor
          ref={editor}
          placeholder="Enter some text..."
          editorState={editorState}
          handleKeyCommand={handleKeyCommand}
          onChange={onChange}
          spellCheck
        />
      </StyledPaper>
    </div>
  );
}

CustomEditor.propTypes = {
  content: PropTypes.object,
  onEditorChange: PropTypes.func,
};

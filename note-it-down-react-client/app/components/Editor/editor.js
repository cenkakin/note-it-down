import { ContentState, Editor, EditorState, RichUtils } from 'draft-js';
import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import StyledPaper from '../../containers/WorkspacePage/StyledPaper';

export default function CustomEditor({ initialContent = '', onEditorChange }) {
  const [editorState, setEditorState] = React.useState(
    EditorState.createWithContent(ContentState.createFromText(initialContent)),
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
      const blocks = editorState.getCurrentContent().getBlocksAsArray();
      onEditorChange(blocks);
    }, 2000);

    return () => clearTimeout(timer);
  }, [editorState]);

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
  initialContent: PropTypes.string,
  onEditorChange: PropTypes.func,
};

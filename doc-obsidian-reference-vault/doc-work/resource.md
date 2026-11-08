- #resource/reading
- #resource/course
- #resource/download
- #resource/success_stories
- convert .md to pdf files
    - ```
      sudo apt install pandoc texlive
      pandoc myfile.md -o output.pdf --pdf-engine=xelatex -V geometry:margin=0.3in
      ```

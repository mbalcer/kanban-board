import {StudentNamePipe} from './student-name.pipe';

describe('StudentNamePipe', () => {
  it('create an instance', () => {
    const pipe = new StudentNamePipe();
    expect(pipe).toBeTruthy();
  });
});

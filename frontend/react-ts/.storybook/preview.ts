import type { Preview } from "@storybook/react";

/* TODO: update import for your custom theme configurations */

/* TODO: replace with your own global styles, or remove */
const GlobalStyles = createGlobalStyle`
  body {
    font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
  }
  `;

const preview: Preview = {
  parameters: {
    controls: {
      matchers: {
        color: /(background|color)$/i,
        date: /Date$/i,
      },
    },
  },

  themes: {},
  Provider: ThemeProvider,
  GlobalStyles,
};

export default preview;

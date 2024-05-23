module.exports = {
  root: true,
  env: { browser: true, es2020: true },
  extends: [
    "standard",
    "prettier",
    "plugin:@typescript-eslint/recommended",
    "plugin:react-hooks/recommended",
    "airbnb",
    "airbnb/hooks",
    "airbnb-typescript",
    "plugin:storybook/recommended"
  ],
  parserOptions: {
    project: "./frontend/react-ts/tsconfig.json",
  },
  ignorePatterns: ["dist", ".eslintrc.cjs"],
  parser: "@typescript-eslint/parser",
  plugins: ["@typescript-eslint"],
  rules: {},
};

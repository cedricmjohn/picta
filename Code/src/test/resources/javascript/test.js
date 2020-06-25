const fs = require('fs');
const path = require("path");

const jsdom = require("jsdom");
const { JSDOM } = jsdom

// get current path
const currentPath = process.cwd();

// get path to the local javascript file
const scriptPath = path.resolve("../../../main/resources/plotly.min.js");

// this succesfully reads in the javascript file
const plotlyJs = fs.readFileSync(scriptPath, "utf8");

// a function that takes in json and validates both the data and layout objects
function validate(traces, layout) {

  // run some function based on the local library
  const dom = new JSDOM(`<body>
  <script>
  window.addEventListener('load', 
    function() {
       window.test = Plotly.validate(${JSON.stringify(traces)}, ${JSON.stringify(layout)})
    }, false);
  </script>

  </body>`, { runScripts: "dangerously" });

  // mock required function
  dom.window.URL.createObjectURL = function() {};

  // inject the local js script onto the page
  const script = dom.window.document.createElement('script');
  script.type = "text/javascript";
  script.textContent = plotlyJs;
  dom.window.document.head.appendChild(script);

  dom.window.onload = () => {
    console.log(JSON.stringify(dom.window.test));
  };
}

process.stdin.on('readable', () => {
  process.stdin.setEncoding('utf8');
  const chart_json = process.stdin.read();
  const chart = JSON.parse(chart_json)
  validate(chart.traces, chart.layout)
});

// const raw = {"traces":[{"name":"test","type":"scatter","mode":"markers+lines","xaxis":"x","yaxis":"y","x":[1,2,3],"y":[1,2,3],"marker":{}}],"layout":{"title":"XY.Scatter.Int","legend":{"x":0,"y":1,"orientation":"v"},"height":500,"width":800,"grid":{"rows":1,"columns":1,"pattern":"independent"},"xaxis":{"title":"x","side":"","overlaying":"","showgrid":true,"zeroline":false,"showline":false},"yaxis":{"title":"y","side":"","overlaying":"","showgrid":true,"zeroline":false,"showline":false}},"config":{"responsive":false,"scrollZoom":true,"displaylogo":false}}
// const chart = JSON.parse(JSON.stringify(raw))

// validate([], chart.layout)

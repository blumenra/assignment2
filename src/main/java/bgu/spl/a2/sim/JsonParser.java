package bgu.spl.a2.sim;

import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class parses the given json file and holds them as his fields.
 * The parsing is done in the constructor with Gson.
 */
public class JsonParser {

    private int threads;
    private List<Tool> tools;
    private Map<Tool, Integer> toolsInventory;
    private List<ManufactoringPlan> plans;
    private List<List<ProductOrder>> waves;

    public JsonParser(String jsonFileName) {
        this.threads = 0;
        this.tools = new ArrayList<>();
        this.toolsInventory = new HashMap<>();
        this.plans = new ArrayList<>();
        this.waves = new ArrayList<>();

        initializeFields(jsonFileName);
    }

    public int getThreads() {
        return threads;
    }

    public List<Tool> getTools() {
        return tools;
    }

    public Map<Tool, Integer> getToolsInventory() {
        return toolsInventory;
    }

    public List<ManufactoringPlan> getPlans() {
        return plans;
    }

    public List<List<ProductOrder>> getWaves() {
        return waves;
    }


    private JSim readFile(String fileName){

        BufferedReader in;
        Gson gsonFile = new Gson();
        JSim jsim = new JSim();
        try {
            in = new BufferedReader(new FileReader(fileName));
            jsim = gsonFile.fromJson(in, JSim.class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return jsim;
    }

    private void initializeFields(String fileName){

        JSim jsim = readFile(fileName);

        this.threads = jsim.getThreads();

        jToolsToToolFieldsInitializer(jsim.getTools());

        this.plans = jsim.getPlans();
        this.waves = jsim.getWaves();

    }

    private void jToolsToToolFieldsInitializer(List<JTool> jToolList) {

        for(JTool jTool : jToolList){

            Tool tool;
            String toolName = jTool.getTool();

            switch (toolName){

                case "gs-driver":
                    tool = new GcdScrewDriver();
                    break;

                case "np-hammer":
                    tool = new NextPrimeHammer();
                    break;

                case "rs-pliers":
                    tool = new RandomSumPliers();
                    break;

                default:
                    throw new RuntimeException("The tool types are wrong");
            }

            this.tools.add(tool);
            this.toolsInventory.put(tool, jTool.getQty());
        }
    }


    private class JTool {

        private String tool;
        private int qty;

        public String getTool() {
            return tool;
        }

        public int getQty() {
            return qty;
        }
    }

    private class JSim {

        private int threads;
        private List<JTool> tools;
        private List<ManufactoringPlan> plans;
        private List<List<ProductOrder>> waves;

        public int getThreads() {
            return threads;
        }

        public List<JTool> getTools() {
            return tools;
        }

        public List<ManufactoringPlan> getPlans() {
            return plans;
        }

        public List<List<ProductOrder>> getWaves() {
            return waves;
        }
    }

}

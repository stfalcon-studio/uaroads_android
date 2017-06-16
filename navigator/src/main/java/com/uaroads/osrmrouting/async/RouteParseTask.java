package com.uaroads.osrmrouting.async;

import android.util.Log;

import com.uaroads.osrmrouting.BusProvider;
import com.uaroads.osrmrouting.callback.RouteParseEvent;
import com.uaroads.osrmrouting.model.Instruction;
import com.uaroads.osrmrouting.model.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alex on 25.12.15.
 */
public class RouteParseTask implements Runnable {
    private final int CIRCLE_INSTRUCTION_START_INDEX = 11;
    private final int CIRCLE_INSTRUCTION_END_INDEX = 15;

    private String json;

    public RouteParseTask(String json) {
        this.json = json;
    }

    @Override
    public void run() {
        try {

            BusProvider.getBus().post(new RouteParseEvent(null, RouteParseEvent.Status.BEGIN));

            final JSONObject object = new JSONObject(json);
            final Route route = parseRoute(object);

            if (route != null) {
                BusProvider.getBus().post(new RouteParseEvent(route, RouteParseEvent.Status.SUCCESS));
                return;
            }

            BusProvider.getBus().post(new RouteParseEvent(null, RouteParseEvent.Status.FAILURE));

        } catch (Exception exc) {
            Log.e("RouteParser", "Unable to parse route from JSON", exc);
            BusProvider.getBus().post(new RouteParseEvent(null, RouteParseEvent.Status.FAILURE));
        }
    }


    private Route parseRoute(JSONObject routeObject) throws JSONException {

        final Route route = new Route();
        route.polyline = routeObject.optString("route_geometry");
        route.geoPointArrayList = route.decodePolylineToPoints();

        final JSONArray nameArray = routeObject.optJSONArray("route_name");
        if (nameArray != null) {
            route.name = getRouteName(nameArray);
        }

        final JSONObject summary = routeObject.optJSONObject("route_summary");
        if (summary != null) {
            route.distance = summary.optLong("total_distance");
            route.duration = summary.optLong("total_time");
        }

        final JSONArray instructions = routeObject.optJSONArray("route_instructions");
        if (instructions != null) {
            route.instructions = parseInstructions(instructions);
        }

        return route;
    }

    private String getRouteName(JSONArray nameArray) throws JSONException {

        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < nameArray.length(); i++) {
            builder.append((String) nameArray.get(i));

            if (i < nameArray.length() - 1) builder.append(" - ");
        }

        return builder.toString();
    }

    private ArrayList<Instruction> parseInstructions(JSONArray instructionsArray) throws JSONException {
        final ArrayList<Instruction> instructions = new ArrayList<>();

        for (int i = 0; i < instructionsArray.length(); i++) {
            instructions.add(parseInstruction(instructionsArray.optJSONArray(i)));
        }

        return getCorrectInstructions(instructions);
    }

    private Instruction parseInstruction(JSONArray instructionArray) throws JSONException {
        if (instructionArray == null) return null;

        final Instruction instruction = new Instruction();

        instruction.direction = instructionArray.getString(Instruction.DIRECTION_INDEX);
        instruction.currentStreetName = instructionArray.getString(Instruction.STREET_NAME_INDEX);
        instruction.distanceToDirection = instructionArray.getLong(Instruction.LENGTH_INDEX);
        instruction.positionInPolyline = instructionArray.getInt(Instruction.POSITION_INDEX);
        instruction.timeToDirection = instructionArray.getLong(Instruction.TIME_INDEX);

        return instruction;
    }

    private ArrayList<Instruction> getCorrectInstructions(ArrayList<Instruction> raw) {
        final ArrayList<Instruction> instructions = new ArrayList<>();

        for (int i = 0; i < raw.size(); i++) {
            final boolean isLast = raw.size() == i + 1;

            if (!isLast) {
                final Instruction current = raw.get(i);
                final Instruction next = raw.get(i + 1);
                final Instruction correct = new Instruction();

                correct.direction = next.direction;
                correct.timeToDirection = current.timeToDirection;
                correct.distanceToDirection = current.distanceToDirection;
                correct.currentStreetName = current.currentStreetName;
                correct.nextStreetName = next.currentStreetName;
                correct.positionInPolyline = next.positionInPolyline;

                instructions.add(correct);
            }
        }

        instructions.get(instructions.size() - 1).isLastInstruction = true;

        return instructions;
    }
}
